import java.util.*;

public class IR3 extends Node {
    ClassData cData;
    ArrayList<MethodData> mData;

    public IR3(Program prog) {
        cData = new ClassData();
        mData = prog.genIR3();
    }

    public ArrayList<Node> getChildren() {
        return null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        String mString = "";
        for(MethodData m: mData) {
            mString = mString + m.toString() + "\n";
        }
        return "===== CData3 ===== \n" +
                cData +
                "\n===== CMtd3 ===== \n" +
                mString;
    }
}

class ClassData extends Node {
    public Hashtable<String, Hashtable<String,String>> rec;

    public ClassData() {
        this.rec = ClassDescriptor.extract();
    }

    public ArrayList<Node> getChildren() {
        return null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        String ret = "";
        String curr = "";
        Hashtable<String,String> varMap;
        for(Map.Entry<String, Hashtable<String,String>> cl: rec.entrySet()) {
            curr = "class " + cl.getKey() + "\n";
            varMap = cl.getValue();
            for(Map.Entry<String,String> var: varMap.entrySet()) {
                curr = curr + "    " + var.getValue() + " " + var.getKey() + "\n";
            }
            ret += curr;
        }
        return ret;
    }
}

class MethodData extends Node {
    //grab from method signature
    String classId;
    String methodId;
    String retType;
    ArrayList<Formal> fmlList;
    ArrayList<Decl3> localVarList;
    ArrayList<Instruction> code;

    public MethodData(String classId, String methodId, String retType, ArrayList<Formal> fmlList, ArrayList<Variable> localVarList, ArrayList<Instruction> code, ArrayList<Decl3> temps) {
        this.classId = classId;
        this.methodId = ClassDescriptor.lookup_flat_func_name(classId, methodId);
        this.retType = retType;
        this.fmlList = fmlList;
        this.localVarList = new ArrayList<Decl3>();
        for(Variable v: localVarList) {
            this.localVarList.add(new Decl3(v.getType(), v.getId()));
        }
        this.localVarList.addAll(temps);
        this.code = code;
    }

    public ArrayList<Node> getChildren() {
        return null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        String codeString = "";
        for(Instruction i: code) {
            codeString += "    " + i + "\n";
        }
        return retType + " " + methodId + " " + fmlList + "\n" +
                localVarList + "\n" +
                codeString;
    }
}

abstract class Instruction {
    Op3 op;
    Arg3 arg1;

    public static Var3 getResultFromList(ArrayList<Instruction> ls) {
        Instruction last = ls.get(ls.size() - 1);
        if(last.isObj()) {
            ls.remove(ls.get(ls.size() - 1));
        }
        return last.getResult();
    }

    public Var3 getResult() {
        return null;
    }

    public String toString() {
        return op + " " + arg1+ ";";
    }

    public boolean isObj() {
        return false;
    }
}
class Obj3 extends Instruction {
    Var3 name;
    Const3 constant;

    public Obj3(String name) {
        this.name = new Var3(name);
    }
    public Obj3(Const3 constant) { this.constant = constant; }

    public boolean isObj() {
        return true;
    }

    public Var3 getResult() {
        return name;
    }

}
class Decl3 extends Instruction {
    public Arg3 arg2;

    public Decl3(Var3 type, Var3 id) {
        super.op = Op3.DECL;
        super.arg1 = type;
        arg2 = id;
    }
    public Decl3(String type, String id) {
        super.op = Op3.DECL;
        super.arg1 = new Var3(type);
        arg2 = new Var3(id);
    }
    public Decl3(Temp t) {
        super.op = Op3.DECL;
        super.arg1 = new Var3(t.getType());
        arg2 = new Var3(t.toString());
    }

    public static void addDecl(ArrayList<Instruction> ls, Temp t) {
        ls.add(0, new Decl3(new Var3(t.getType()), new Var3(t.toString())));
    }

    public String toString() {
        return arg1 + " " + arg2;
    }
}
class Bexp3 extends Instruction {
    public Arg3 arg2;
    public Var3 result;

    public Bexp3(Var3 result, Arg3 arg1, Arg3 arg2, Op3 op3) {
        super.op = op3;
        super.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @java.lang.Override
    public Var3 getResult() {
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return result + " = " +  arg1 + " " + super.op + " " + arg2+ ";";
    }
}
class Uexp3 extends Instruction {
    public Var3 result;

    public Uexp3(Var3 result, Arg3 arg1, Op3 op3) {
        super.arg1 = arg1;
        super.op = op3;
        this.result = result;
    }

    @java.lang.Override
    public Var3 getResult() {
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return result + " = " + super.op + " " + arg1+ ";";
    }
}
class Jmp3 extends Instruction {

    public Jmp3(Int3 labelNum) {
        super.op = Op3.GOTO;
        super.arg1 = labelNum;
    }
}
class CJmp3 extends Instruction {
    public Arg3 arg2;

    public CJmp3(Var3 test, Int3 labelNum) {
        super.op = Op3.IF;
        super.arg1 = test;
        this.arg2 = labelNum;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return super.op + " "+ arg1 + " goto " + arg2+ ";";
    }
}
//call, storing result
class Param3 extends Instruction {
    public Param3(Arg3 arg1) {
        super.op = Op3.PARAM;
        super.arg1 = arg1;
    }
}
class ECall3 extends Instruction {
    public Arg3 arg2;
    public Var3 result;

    public ECall3(Var3 result, Var3 funcName, VarList3 args) {
        super.op = Op3.CALL;
        super.arg1 = funcName;
        this.arg2 = args;
        this.result = result;
    }

    @java.lang.Override
    public Var3 getResult() {
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return result + " = " + super.arg1 + " " + arg2+ ";";
    }
}
//call, discarding result
class SCall3 extends Instruction {
    public Arg3 arg2;

    public SCall3(Var3 funcName, VarList3 args) {
        super.op = Op3.CALL;
        super.arg1 = funcName;
        this.arg2 = args;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return super.arg1 + "" + arg2 + ";";
    }
}
class Label3 extends Instruction {
    private static int num = 0;

    private int n;
    public Label3() {
        super.op = Op3.LABEL;
        this.n = num;
        super.arg1 = new Int3(this.n);
        num++;
    }
    public int getN() {
        return n;
    }

    public String toString() {
        return super.op.toString() + arg1 + ":";
    }
}
class Assign3 extends Instruction {
    public Var3 result;

    public Assign3(Var3 result, Arg3 val) {
        super.op = Op3.ASSIGN;
        super.arg1 = val;
        this.result = result;
    }

    @java.lang.Override
    public Var3 getResult() {
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return result + " " + super.op + " " + arg1 + ";";
    }
}
class Read3 extends Instruction {
    public Read3(Var3 var) {
        super.op = Op3.READ;
        super.arg1 = var;
    }
}
class Print3 extends Instruction {
    public Print3(Arg3 arg1) {
        super.op = Op3.PRINT;
        super.arg1 = arg1;
    }
}
class Ret3 extends Instruction {
    public Ret3() {
        super.op = Op3.RET;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return super.op + ";";
    }
}
class VRet3 extends Instruction {
    public VRet3(Var3 arg1) {
        super.op = Op3.VRET;
        super.arg1 = arg1;
    }
}

/*
abstract class Op3 {

}
class Bop3 extends Op3 {

}
class Uop3 extends Op3 {

}
class ADD3 extends Bop3 {

}
class SUB3 extends Bop3 {

}
class MULT3 extends Bop3 {

}
class DIV3 extends Bop3 {

}
class NEG3 extends Uop3 {

}
class LE3 extends Uop3 {

}
class GE3 extends Bop3 {

}
class LT3 extends Bop3 {

}
class GT3 extends Bop3 {

}
class EQ3 extends Bop3 {

}
class AND3 extends Bop3 {

}
class OR3 extends Bop3 {

}
class NOT3 extends Uop3 {

}
class IF3 extends Op3 {

}
class ASSIGN3 extends Op3 {

}
class CALL3 extends Op3 {

}
class GOTO3 extends Op3 {

}
class LABEL3 extends Op3 {

}
class Read3 extends Op3 {

}
class Print3 extends Op3 {

}
class Ret3 extends Op3 {

}
class VRet3 extends Op3 {

}
*/

abstract class Arg3 {
    public boolean isVar3() {return false;}
}
class Var3 extends Arg3 {
    private String id;

    public Var3() {
    }

    public Var3(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public boolean isVar3() {return true;}
}
class VarList3 extends Arg3 {
    private ArrayList<Var3> varList;

    public VarList3() {
        varList = new ArrayList<Var3>();
    }
    public VarList3(ArrayList<Var3> varList) {
        this.varList = varList;
    }
    public void add(Var3 var) {
        varList.add(var);
    }
    public void add(int index, Var3 var) {
        varList.add(index, var);
    }
    public String toString() {
        return varList.toString();
    }
}
class New3 extends Arg3 {
    String classId;

    public New3(String classId) {
        this.classId = classId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "new " + classId + "()";
    }
}
class Tag3 extends Arg3 {

}
class Const3 extends Arg3 {

}
class Access3 extends Arg3 {
    String objectId;
    String fieldId;

    public Access3(String objectId, String fieldId) {
        this.objectId = objectId;
        this.fieldId = fieldId;
    }
    public String toString() {
        return objectId + "." + fieldId;
    }
}
class Bool3 extends Const3 {
    boolean val;

    public Bool3(boolean val) {
        this.val = val;
    }

    public String toString() {
        return String.valueOf(val);
    }
}
class Int3 extends Const3 {
    int val;

    public Int3(int val) {
        this.val = val;
    }

    public String toString() {
        return String.valueOf(val);
    }
}
class Str3 extends Const3 {
    String val;

    public Str3(String val) {
        this.val = val;
    }

    public String toString() {
        return "\"" + val + "\"";
    }
}