package codegen;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import ir3.Access3;
import ir3.Arg3;
import ir3.ClassData;
import ir3.Const3;
import ir3.Int3;
import ir3.MethodData;
import ir3.Str3;
import ir3.Var3;

import static codegen.MemOp.*;
import static codegen.MemSrc.*;

public class RDAD {
    private ArrayList<HashSet<Arg3>> regDescriptor;

    private static int fixedFrameOffset = 6;
    private int dynamicOffsetAccum;
    private int lastFormalIndex;
    private ArrayList<HashSet<Integer>> addrDescriptor;
    private HashMap<Access3, Integer> accessDescriptor;
    private BitSet memMap;
    private HashMap<Var3, Integer> formalIndexMap;
    private HashMap<Var3, Integer> localIndexMap;
    private HashMap<Var3, Integer> formalOffsetMap;
    private HashMap<Var3, Integer> localDynamicOffsetMap;

    private MethodData methodData;

    public RDAD(ArrayList<Var3> formals, ArrayList<Var3> locals, ArrayList<Var3> temps,
                MethodData methodData) {
        this.methodData = methodData;
        this.accessDescriptor = new HashMap<>();
        regDescriptor = new ArrayList<>(10);
        for (int i = 0; i < 10; i++)
            regDescriptor.add(new HashSet<>());
        int n = (formals.size() > 4) ? 4 : formals.size();
        for (int i = 0; i<n; i++) {
            regDescriptor.get(i).add(formals.get(i));
        }

        formalOffsetMap = new HashMap<>();
        localDynamicOffsetMap = new HashMap<>();
        dynamicOffsetAccum = 4;

        addrDescriptor = new ArrayList<>(formals.size() + locals.size() + temps.size());
        memMap = new BitSet(formals.size() + locals.size() + temps.size());
        formalIndexMap = new HashMap<>();
        localIndexMap = new HashMap<>();

        int a = 0;
        for(Var3 v: formals) {
            formalIndexMap.put(v,a);
            a++;
        }
        a = 0;
        for(Var3 v: locals) {
            localIndexMap.put(v,a);
            a++;
        }
        for(Var3 v: temps) {
            localIndexMap.put(v,a);
            a++;
        }
        a = 0;
        int n = (formals.size() > 4) ? 4 : formals.size();
        for(int i=0; i<n; i++) {
            addrDescriptor.get(i).add(a);
            a++;
        }
        memMap.clear();
        //initialize memory for actuals beyond 4.
        if(formals.size() > 4) {
            for(int i = 4; i<formals.size(); i++) {
                memMap.set(i);
            }
        }

        lastFormalIndex = formals.size() - 1;

    }

    public Integer calculateReadOffset(Var3 variable, boolean isFormal) {
        Integer index, offset;
        if(isFormal) {
            index = formalIndexMap.get(variable);
            if(index > 3) {
                offset = lastFormalIndex - index + 1;
                return offset;
            } else {
                offset = formalOffsetMap.get(variable);
                if(offset != null)
                    return offset;
                offset = - (dynamicOffsetAccum + fixedFrameOffset);
                dynamicOffsetAccum += 4;
                formalOffsetMap.put(variable, offset);
                return offset;
            }
        } else {
            offset = -(dynamicOffsetAccum + fixedFrameOffset);
            dynamicOffsetAccum += 4;
            localDynamicOffsetMap.put(variable, offset);
        }
        return offset;
    }

    public Access getRegForAccess(Access3 elem, Var3 read) {
        Access access = getRegForAccess(null, elem);
        access.setOp(WRITE);
        return access;
    }

    public Access getRegForAccess(Var3 def, Access3 elem) {
        Var3 object = new Var3(elem.objectId);
        Integer offset = ClassOffsetMap.getOffset(ClassData.getType(methodData.classId, elem.objectId), elem.fieldId);

        //field name can collide with formal and local names
        Access accessRecordPointer;
        Access accessField = getAvailableReg(elem);
        if(accessField != null)
            return accessField;
        accessRecordPointer = getReg(null, object, null, 2);
        accessField = getEmptyReg(elem);
        if(accessField == null)
            accessField = getFullRegByMultiplicity(elem);
        if(accessField == null)
            accessField = getFullRegByLiveness(def, elem);
        if(accessField == null)
            accessField = getFullRegBySpill(elem);
        ((Mem)accessField).setGetOffset(offset);

        accessRecordPointer.setOp(READ);
        accessRecordPointer.addFieldAccess(accessField);
        return accessRecordPointer;
    }

    //available register? that reg: empty register? Reg with offset: full reg? that reg: spill;
    //to do : update ad and rd
    public Access getReg(Var3 def, Var3 v1, Var3 v2, int num) {
        Access access;
        Var3 variable = num == 1? def : num == 2 ? v1 : v2;

        //read from heap!
        if(methodData.isOnHeap(variable)) {
            access = getRegForAccess(null, new Access3("this", variable.getId()));
            return access;
        }
        
        access = getAvailableReg(variable);
        if(access == null)
            access = getEmptyReg(variable);
        if(access == null)
            access = getFullRegByMultiplicity(variable);
        if(access == null && num != 1)
            access = getFullRegByLiveness(def, v1, v2, num);
        if(access == null)
            access = getFullRegBySpill(variable);

        if(num == 1) {
            access.setOp(WRITE);
            markAsDirty(variable);
        }
        else {
            access.setOp(READ);
        }
        return access;
    }

    //see if variable is in some register
    private Access getAvailableReg(Var3 variable) {
        Integer index = getVarIndex(variable);
        HashSet<Integer> description = addrDescriptor.get(index);
        if(!description.isEmpty()) {
            //take the first good register
            for(Integer i : description)
                return new Reg(getArmRegNum(i));
        }
        return null;
    }

    private Access getAvailableReg(Access3 acc) {
        Integer regNum = accessDescriptor.get(acc);
        if(regNum != null)
            return new Reg(getArmRegNum(regNum));
        return null;
    }

    private Access getEmptyReg(Var3 variable) {
        Integer offset;
        Integer regNum;
        for (HashSet<Arg3> register : regDescriptor) {
            if (register.isEmpty()) {
                if (variable instanceof Const3) {
                    regNum = regDescriptor.indexOf(register);
                    setRegDescriptor(regNum, variable);
                    return new Reg(getArmRegNum(regNum));
                } else {
                    offset = getReadOffset(variable);
                    regNum = regDescriptor.indexOf(register);
                    setRegDescriptor(regNum, variable);
                    addRegToAddrDescriptor(regNum, variable);
                    return new Mem(getArmRegNum(regNum), offset, DUMMYOP, STACK);
                }
            }
        }
        return null;
    }

    private Access getEmptyReg(Access3 acc) {
        Integer regNum;
        for (HashSet<Arg3> register : regDescriptor) {
            if(register.isEmpty()) {
                regNum = regDescriptor.indexOf(register);
                setRegDescriptor(regNum, acc);
                addRegToAccessDescriptor(regNum, acc);
                return new Mem(getArmRegNum(regNum), -1, DUMMYOP, DUMMYSRC);
            }
        }
        return null;
    }

    private Access getFullRegByMultiplicity(Var3 variable) {
        TreeSet<HashSet<Arg3>> candidates = new TreeSet<>(new RegComparator());
        Integer regNum;
        //least references sort
        for(HashSet<Arg3> register: regDescriptor) {
            candidates.add(register);
        }
        //least number of conflicting items sort
        boolean ok;
        for(HashSet<Arg3> reg: candidates) {
            ok = true;
            for (Arg3 arg : reg) {
                if (arg instanceof Var3 && !lookup_var((Var3) arg))
                    ok = false;
            }
            if (ok) {
                regNum = regDescriptor.indexOf(reg);
                for (Arg3 arg : reg) {
                    minusRegFromAddrDescriptor(regNum, arg);
                }
                setRegDescriptor(regNum, variable);
                if(variable instanceof Const3)
                    return generateConstAccess(regNum, (Const3)variable);
                addRegToAddrDescriptor(regNum, variable);
                return new Mem(getArmRegNum(regNum), getReadOffset(variable), DUMMYOP, STACK);
            }
        }
        return null;
    }

    private Access getFullRegByMultiplicity(Access3 acc) {
        TreeSet<HashSet<Arg3>> candidates = new TreeSet<>(new RegComparator());
        Integer regNum;
        //least references sort
        for(HashSet<Arg3> register: regDescriptor) {
            candidates.add(register);
        }
        //least number of conflicting items sort
        boolean ok;
        for(HashSet<Arg3> reg: candidates) {
            ok = true;
            for (Arg3 arg : reg) {
                if (arg instanceof Var3 && !lookup_var((Var3) arg))
                    ok = false;
            }
            if (ok) {
                regNum = regDescriptor.indexOf(reg);
                for (Arg3 arg : reg) {
                    minusRegFromAddrDescriptor(regNum, arg);
                }
                setRegDescriptor(regNum, acc);
                addRegToAccessDescriptor(regNum, acc);
                return new Mem(getArmRegNum(regNum), -1, DUMMYOP, DUMMYSRC);
            }
        }
        return null;
    }

    //only for read
    private Access getFullRegByLiveness(Var3 def, Var3 v1, Var3 v2, int num) {
        Var3 v = num == 1? def : num == 2? v1 : v2;
        Integer regNum;
        TreeSet<HashSet<Arg3>> candidates = new TreeSet<>(new RegComparator());
        //least references sort
        for(HashSet<Arg3> register: regDescriptor) {
            candidates.add(register);
        }
        boolean ok;
        for(HashSet<Arg3> reg: candidates) {
            ok = true;
            for(Arg3 arg : reg) {
                if(!(!v.equals(def) && arg.equals(def) && !def.equals(v1) && !def.equals(v2)))
                    ok = false;
            }
            if(ok) {
                regNum = regDescriptor.indexOf(reg);
                for (Arg3 arg : reg) {
                    minusRegFromAddrDescriptor(regNum, arg);
                }
                setRegDescriptor(regNum, v);
                addRegToAddrDescriptor(regNum, v);
                return new Mem(getArmRegNum(regNum), getReadOffset(v), DUMMYOP, STACK);
            }
        }
        return null;
    }

    private Access getFullRegByLiveness(Var3 def, Access3 acc) {
        Integer regNum;
        TreeSet<HashSet<Arg3>> candidates = new TreeSet<>(new RegComparator());
        //least references sort
        for(HashSet<Arg3> register: regDescriptor) {
            candidates.add(register);
        }
        boolean ok;
        for(HashSet<Arg3> reg: candidates) {
            ok = true;
            for(Arg3 arg : reg) {
                if(!arg.equals(def))
                    ok = false;
            }
            if(ok) {
                regNum = regDescriptor.indexOf(reg);
                for (Arg3 arg : reg) {
                    minusRegFromAddrDescriptor(regNum, arg);
                }
                setRegDescriptor(regNum, acc);
                addRegToAccessDescriptor(regNum, acc);
                return new Mem(getArmRegNum(regNum), -1, DUMMYOP, DUMMYSRC);
            }
        }
        return null;
    }
    
    private Access getFullRegBySpill(Var3 variable) {
        TreeSet<HashSet<Arg3>> candidates = new TreeSet<>(new RegComparator());
        Integer regNum;
        //least conflicting items
        for(HashSet<Arg3> register: regDescriptor) {
            candidates.add(register);
        }
        HashSet<Arg3> reg = candidates.first();
        Var3 v;
        //read offset
        Integer offset = getReadOffset(variable);
        regNum = regDescriptor.indexOf(reg);

        Access access = new Mem(regNum, offset, DUMMYOP, STACK);
        for(Arg3 arg : reg) {
            if(arg instanceof Var3 && !(arg instanceof Const3)) {
                v = (Var3)arg;
                //spill offset
                offset = getReadOffset(v);
                if(offset != null)
                    access.addSpill(offset);
                markAsUpdated(v);
            }
            minusRegFromAddrDescriptor(regNum, arg);
        }
        setRegDescriptor(regNum, variable);
        addRegToAddrDescriptor(getArmRegNum(regNum), variable);
        return access;
    }


    private Access getFullRegBySpill(Access3 acc) {
        TreeSet<HashSet<Arg3>> candidates = new TreeSet<>(new RegComparator());
        Integer regNum;
        Integer spillOffset;
        //least conflicting items
        for(HashSet<Arg3> register: regDescriptor) {
            candidates.add(register);
        }
        HashSet<Arg3> reg = candidates.first();
        Var3 v;
        regNum = regDescriptor.indexOf(reg);

        Access access = new Mem(regNum, -1, DUMMYOP, DUMMYSRC);
        for(Arg3 arg : reg) {
            if(arg instanceof Var3 && !(arg instanceof Const3)) {
                v = (Var3)arg;
                //spill offset
                spillOffset = getReadOffset(v);
                if(spillOffset != null)
                    access.addSpill(spillOffset);
                markAsUpdated(v);
            }
            minusRegFromAddrDescriptor(regNum, arg);
        }
        setRegDescriptor(regNum, acc);
        addRegToAccessDescriptor(getArmRegNum(regNum), acc);
        return access;
    }

    private Access generateConstAccess(Integer regNum, Const3 data) {
        int label;
        if(data instanceof Int3) {
            if(((Int3) data).getVal() > 256) {
                label = DataLabel.gen();
                return new Mem(regNum, label, STATIC);
            } else {
                return new Reg(regNum);
            }
        }
        else if(data instanceof Str3){
            label = DataLabel.gen();
            return new Mem(regNum, label, STATIC);
        } else {
            return new Reg(regNum);
        }
    }

    //check if variable has locations other than the 1 reg
    public boolean lookup_var(Var3 variable) {
        Integer index = getVarIndex(variable);
        if(index == null)
            return false;
        if(memMap.get(index) || addrDescriptor.get(index).size() > 1)
            return true;
        else
            return false;
    }

    public Integer getVarIndex(Var3 var) {
        boolean isLocal = methodData.isLocal(var);
        return isLocal? localIndexMap.get(var) : formalIndexMap.get(var);
    }

    public Integer getReadOffset(Var3 var) {
        Integer offset;
        if(methodData.isLocal(var)) {
            offset = localDynamicOffsetMap.get(var);
            if (offset == null)
                return calculateReadOffset(var, false);
        }
        else if(methodData.isFormal(var)) {
            offset = formalOffsetMap.get(var);
            if (offset == null)
                return calculateReadOffset(var, true);
        }
        else
            return null;

        return null;
    }

    private void setRegDescriptor(Integer regNum, Arg3 var) {
        HashSet<Arg3> reg = regDescriptor.get(regNum);
        reg.clear();
        reg.add(var);
    }

    private void addRegToAddrDescriptor(Integer regNum, Var3 variable) {
        Integer index = getVarIndex(variable);
        if(index != null)
            addrDescriptor.get(index).add(regNum);
    }

    private void addRegToAccessDescriptor(Integer regNum, Access3 acc) {
        accessDescriptor.put(acc, regNum);
    }

    private void minusRegFromAddrDescriptor(Integer regNum, Arg3 arg) {
        Var3 variable;
        if(arg instanceof Var3 && !(arg instanceof Const3)) {
            variable = (Var3) arg;
            Integer index = getVarIndex(variable);
            addrDescriptor.get(index).remove(regNum);
        }
    }

    private Integer getArmRegNum(Integer index) {
        if(index == 9)
            return 14;
        else
            return index;
    }

    private void markAsDirty(Var3 variable) {
        Integer offset = getReadOffset(variable);
        if(offset != null)
            memMap.clear(offset);
    }
    private void markAsUpdated(Arg3 arg) {
        Var3 variable;
        Integer offset;
        if(arg instanceof Var3) {
            variable = (Var3) arg;
            offset = getReadOffset(variable);
        }
        if(offset != null)
            memMap.set(offset);
    }
}

class RegComparator implements Comparator<HashSet<Arg3>> {
    @Override
    public int compare(HashSet<Arg3> o1, HashSet<Arg3> o2) {
        if(o1.size() < o2.size())
            return -1;
        else if (o1.size() == o2.size())
            return 0;
        else
            return 1;
    }
}