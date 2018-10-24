import java.util.*;

public class ClassDescriptor {
    public static Hashtable<String, Tuple<VarSig, MSig>>
            cdesc = new Hashtable<String, Tuple<VarSig, MSig> > ();

    public static Tuple<VarSig, MSig> lookup(String cName) {
        return cdesc.get(cName);
    }

    public static Type lookup_field_type(String cName, String vName) {
        Tuple<VarSig, MSig> tup = cdesc.get(cName);
        if(tup == null)
            return null;
        String ret = tup.x.rec.get(vName);
        if(ret == null)
            return null;
        return new Type(ret);
    }

    public static Tuple<HashMap<String,String>, String> lookup_method_sig(String cName, String mName) {
        return cdesc.get(cName).y.rec.get(mName);
    }

    public static String lookup_flat_func_name(String cName, String mName) {
        return cdesc.get(cName).y.lookup_flat_name(mName);
    }

    public static void flattenFuncNames() {
        for(Map.Entry<String, Tuple<VarSig, MSig>> entry: cdesc.entrySet()) {
            MSig mMap = entry.getValue().y;
            mMap.assignFlatName(entry.getKey());
        }
    }

    //return a shallow copy of classes and their variable signatures
    public static Hashtable<String, Hashtable<String,String>> extract() {
        Hashtable<String, Hashtable<String,String>> ret = new Hashtable<String, Hashtable<String,String>>();
        String cName;
        Hashtable<String,String> vSig;
        for(Map.Entry<String, Tuple<VarSig, MSig>> entry: cdesc.entrySet()) {
            cName = entry.getKey();
            vSig = new Hashtable<String,String>(entry.getValue().x.rec);
            ret.put(cName, vSig);
        }
        return ret;
    }

    public static int insert(MyClass c) {
        //class name distinctness check
        if(cdesc.containsKey(c.id)) {
            return 1;
        }

        VarSig vsig = new VarSig();
        MSig msig = new MSig();

        //process variable signatures
        ArrayList<Variable> vList = c.vs.vars;
        String vType, vName;
        for(int i=0; i<vList.size(); i++) {
            vType = vList.get(i).type;
            vName = vList.get(i).id;
            if(vsig.rec.containsKey(vName)) {
                return 3;
            }
            vsig.rec.put(vName, vType);
        }

        //process method signatures
        Method m;
        String mName, rType;
        Formal formal;
        String fName, fType;
        ArrayList<Method> mList = c.ms.ms;
        ArrayList<Formal> fList;

        HashMap<String, String> fmlMap;
        Tuple<HashMap<String, String>, String> mInfo;
        for(int i=0; i<mList.size(); i++) {
            m = mList.get(i);
            mName = m.id;
            rType = m.type;

            fList = m.formals.formals;
            fmlMap = new LinkedHashMap<String, String>();
            for(int j=0; j<fList.size(); j++) {
                formal = fList.get(j);
                fName = formal.id;
                fType = formal.type;
                //formal name distinctness check
                if(fmlMap.containsKey(fName)) {
                    return 2;
                }
                fmlMap.put(fName, fType);
            }

            mInfo = new Tuple<HashMap<String,String>, String> (fmlMap, rType);

            if(msig.rec.containsKey(mName)) {
                if(!canOverload(mInfo, msig.rec.get(mName))) {
                    return 4;
                }
            }

            msig.rec.put(mName, mInfo);
        }

        //update class descriptor
        cdesc.put(c.id, new Tuple<VarSig, MSig>(vsig, msig));
        return 0;
    }

    public static boolean canOverload(Tuple<HashMap<String, String>, String> one, Tuple<HashMap<String, String>, String> two) {
        if(!one.y.equals(two.y)) {
            return false;
        }
        if(one.x.size() != two.x.size()) {
            return true;
        }
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        Collection<String> one_types = one.x.values();
        for(String v: one_types) {
            if(map.containsKey(v)) {
                map.put(v, map.get(v) + 1);
            }
            map.put(v, 1);
        }
        Collection<String> two_types = two.x.values();
        for(String v: two_types) {
            if(!map.containsKey(v)) {
                return true;
            }
            if(map.get(v) == 1) {
                map.remove(v);
            } else {
                map.put(v, map.get(v) - 1);
            }
        }
        if(map.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static String asString() {
        return cdesc.toString();
    }
}

class VarSig {
    public Hashtable<String, String> rec;

    public VarSig() {
        this.rec = new Hashtable<String, String>();
    }
}

class MSig {
    public Hashtable<String, String> nameMap;

    public Hashtable<String, Tuple<HashMap<String,String>, String>> rec;

    public MSig() {
        this.nameMap = new Hashtable<String, String>();
        this.rec = new Hashtable<String, Tuple<HashMap<String,String>, String>>();
    }

    public void assignFlatName(String classId) {
        int i=0;
        for(String k: rec.keySet()) {
            this.nameMap.put(k, "%"+classId+"_"+String.valueOf(i));
            i++;
        }
    }

    public String lookup_flat_name(String name) {
        return nameMap.get(name);
    }
}
