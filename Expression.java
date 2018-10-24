import java.util.*;

abstract class Expression extends Node {
    //abstract Value eval();

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return null;
    }

    public abstract Type typecheck(LocalEnvironment env);

    public abstract ArrayList<Instruction> genIR3(String classId);
}

class Bool extends Expression {
    public boolean val;

    public Bool(boolean b) {
        val = b;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return Printable.parens("Bool " + val);
    }

    public Type typecheck(LocalEnvironment env) {
        return new Type("Bool");
    }

    @Override
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("Bool");
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new Bool3(val)));
        return ret;
    }
}

class Int extends Expression {
    public int val;

    public Int(int a) {
        val = a;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return Printable.parens("Int " + val);
    }

    public Type typecheck(LocalEnvironment env) {
        return new Type("Int");
    }
    @Override
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("Int");
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new Int3(val)));
        return ret;
    }
}

class Str extends Expression {
    public String val;

    public Str(String s) {
        val = s;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return Printable.parens("Str " + Printable.parens(val));
    }

    public Type typecheck(LocalEnvironment env) {
        return new Type("String");
    }
    @Override
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("Str");
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new Str3(val)));
        return ret;
    }
}

class BinExp extends Expression {
    public Op op;
    public Expression l;
    public Expression r;
    public Type type;

    public BinExp(Op op, Expression l, Expression r) {
        this.op = op;
        this.l = l;
        this.r = r;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(l);
        ls.add(r);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "BinExp " + op;
    }

    public Type typecheck(LocalEnvironment env) {
        Type l_type = l.typecheck(env);
        Type r_type = r.typecheck(env);
        if(op.getType().equals("Arith") && l_type.equals(new Type("Int")) && r_type.equals(new Type("Int"))) {
            type = new Type("Int");
            return type;
        }
        else if(op.getType().equals("Boolean") && l_type.equals(new Type("Bool")) && r_type.equals(new Type("Bool"))) {
            type = new Type("Bool");
            return type;
        }
        else if(op.getType().equals("Rel") && l_type.equals(new Type("Int")) && r_type.equals(new Type("Int"))) {
            type = new Type("Bool");
            return type;
        } else if (!l_type.equals(new Type("Bool")) && !l_type.equals(new Type("Int"))){
            type = new Error_t("l expression does not have bool or int type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else if (!r_type.equals(new Type("Bool")) && !r_type.equals(new Type("Int"))){
            type = new Error_t("r expression does not have bool or int type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else if (!l_type.equals(r_type)){
            type = new Error_t("l expression has different type from r", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else if (!op.getType().equals("Rel") && !op.getType().equals("Arith")) {
            type = new Error_t("Invalid binary operator", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else {
            type = new Error_t("Error in binary expression", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> codeL = l.genIR3(classId);
        ArrayList<Instruction> codeR = r.genIR3(classId);
        Var3 resL = Instruction.getResultFromList(codeL);
        Var3 resR = Instruction.getResultFromList(codeR);

        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        ret.addAll(codeL);
        ret.addAll(codeR);
        ret.add(new Bexp3(t,resL,resR, op.getOp3()));
        return ret;
    }
}

class UExp extends Expression {
    public Op op;
    public Expression exp;
    public Type type;

    public UExp(Op op, Expression exp) {
        this.op = op;
        this.exp = exp;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(exp);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "UExp " + op;
    }

    public Type typecheck(LocalEnvironment env) {
        Type exp_type = exp.typecheck(env);
        if(op.getType().equals("Arith") && exp_type.equals("Int")) {
            type = new Type("Int");
            return type;
        }
        else if(op.getType().equals("Boolean") && exp_type.equals("Bool")) {
            type = new Type("Bool");
            return type;
        }
        else {
            type = new Error_t("Error in unary expression", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId);
        Var3 res = Instruction.getResultFromList(code);

        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        ret.addAll(code);
        ret.add(new Uexp3(t,res,op.getOp3()));
        return ret;
    }
}

abstract class Atom extends Expression {
    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return null;
    }

    //recursion depth of atom is at most 2 for dispatch, use the following functions to force error for non-id type
    public boolean isId() {
        return false;
    }
    public boolean isAccess() {
        return false;
    }
}

class This extends Atom {
    public Type type;

    public This() {
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "This";
    }

    public Type typecheck(LocalEnvironment env) {
        type = env.lookup_var("this");
        return type;
    }

    @Override
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new Var3("this")));
        return ret;
    }
}
class Null extends Atom {
    public Null() {
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "Null";
    }

    public Type typecheck(LocalEnvironment env) {
        return new Null_t();
    }
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("null");
        ret.add(new Assign3(t, new Var3("null")));
        return ret;
    }
}

class ExpParen extends Atom {
    public Expression exp;
    public Type type;

    public ExpParen(Expression exp) {
        this.exp = exp;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(exp);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return Printable.parens(exp.toString());
    }

    public Type typecheck(LocalEnvironment env) {
        type = exp.typecheck(env);
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId);
        Var3 res = Instruction.getResultFromList(code);

        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        ret.addAll(code);
        ret.add(new Assign3(t,res));
        return ret;
    }
}

class Id extends Atom {
    public String objectId;
    public Type objectType;

    public Id(String objectId) {
        this.objectId = objectId;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return Printable.parens("Id " + objectId);
    }

    @Override
    public boolean isId() {
        return true;
    }

    public Type typecheck(LocalEnvironment env) {
        Type t = env.lookup_var(objectId);
        if(t == null) {
            objectType = new Error_t("Unknown variable binding " + objectId, env.currMethod, env.currClass);
            objectType.printMsg();
            return objectType;
        }
        objectType = t;
        return t;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp(objectType);
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new Var3(objectId)));
        return ret;
    }
}

class New extends Atom {
    public String typeId;
    public Type type;

    public New(String typeId) {
        this.typeId = typeId;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "New " + typeId;
    }

    public Type typecheck(LocalEnvironment env) {
        if(ClassDescriptor.lookup(typeId) == null) {
            type = new Error_t("Constructor of unknown class " + typeId, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type = new Type(typeId);
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp(typeId);
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new New3(typeId)));
        return ret;
    }
}

class Access extends Atom {
    public Atom obj;
    public String fieldName;
    public Type type;

    public Access(Atom obj, String fieldName) {
        this.obj = obj;
        this.fieldName = fieldName;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(obj);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "Access " + fieldName;
    }

    @Override
    public boolean isAccess() {
        return true;
    }

    public Type typecheck(LocalEnvironment env) {
        if(!obj.isId()) {
            type = new Error_t("Accessing non-object", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type object_type = env.lookup_var(((Id)obj).objectId);
        //no such binding
        if(object_type == null) {
            type = new Error_t("Unknown variable binding", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Tuple<VarSig, MSig> classInfo = ClassDescriptor.lookup(object_type.getId());
        //no such class
        if(classInfo == null) {
            type = new Error_t("Class not found " + object_type, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type field_type = ClassDescriptor.lookup_field_type(object_type.getId(),fieldName);
        //no such field
        if(field_type == null) {
            type = new Error_t("No such field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type = field_type;
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        String objectName = ((Id)obj).objectId;
        ret.add(new Assign3(t,
                new Access3(objectName, fieldName)));
        return ret;
    }
}

class Dispatch extends Atom {
    public Atom obj;
    public Expressions exps;
    public Type type;

    public Dispatch(Atom obj, Expressions exps) {
        this.obj = obj;
        this.exps = exps;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(obj);
        ls.add(exps);
        return ls;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Dispatch";
    }

    public Type typecheck(LocalEnvironment env) {
        int res = 0;
        Type exp_type;
        String mName;
        Tuple<HashMap<String, String>, String> methodSig;
        HashMap<String, String> formalMap;
        ArrayList<Expression> expList = exps.exps;

        String object_name;
        Type object_type;
        Tuple<VarSig, MSig> class_info;
        //obj is of class Id, expressions match method signature
        if (obj.isId()) {
            mName = ((Id) obj).objectId;
            methodSig = env.lookup_mtd(mName);
            if (methodSig != null) {
                formalMap = methodSig.x;
                res = exps.check_exps(formalMap, env);
                if (res == 0) {
                    type = new Type(methodSig.y);
                    return type;
                } else {
                    this.reportResult(res, env, mName);
                    return type;
                }
            }
            type = new Error_t("Method signature not found" + mName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        //obj is of class Access, obj.obj is of class Id, expressions match
        // CD.lookup(env.lookup(obj.obj.objectId)).y.rec.get(obj.fieldName)
        if (obj.isAccess() && ((Access) obj).obj.isId()) {
            object_name = ((Id) ((Access) obj).obj).objectId;
            object_type = ((Id) ((Access) obj).obj).typecheck(env);
            if (object_type == null) {
                type = new Error_t("Unknown variable binding " + object_name, env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
            class_info = ClassDescriptor.lookup(object_type.getId());
            if (class_info == null) {
                type = new Error_t("Unknown class " + object_type, env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
            mName = ((Access) obj).fieldName;
            methodSig = ClassDescriptor.lookup_method_sig(object_type.getId(), mName);
            formalMap = methodSig.x;
            res = exps.check_exps(formalMap, env);
            if (res == 0) {
                type = new Type(methodSig.y);
                return type;
            } else {
                this.reportResult(res, env, mName);
                return type;
            }
        }
        type = new Error_t("Method call is ill-formed", env.currMethod, env.currClass);
        type.printMsg();
        return type;
    }

    private void reportResult(int res, LocalEnvironment env, String mName) {
        if (res == 1) {
            type = new Error_t("Args list has wrong cardinality when calling " + mName, env.currMethod, env.currClass);
            type.printMsg();
        } else if (res == 2) {
            type = new Error_t("Some arg expression does not typecheck when calling " + mName, env.currMethod, env.currClass);
            type.printMsg();
        } else if (res == 3) {
            type = new Error_t("Arg and formal type mismatch when calling" + mName, env.currMethod, env.currClass);
            type.printMsg();
        }
    }

    @Override
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> params = new ArrayList<Instruction>();
        ArrayList<Instruction> code = new ArrayList<Instruction>();
        Var3 res;

        //callClassId is for dynamic dispatch only
        Type callClassId;
        String callObjectId;
        String funcName;

        for (Expression e : exps.exps) {
            code = e.genIR3(classId);
            ret.addAll(code);
            res = Instruction.getResultFromList(code);
            params.add(new Param3(res));
        }
        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        if (obj.isId()) {
            params.add(0, new Param3(new Var3("this")));
            ret.addAll(params);
            funcName = ClassDescriptor.lookup_flat_func_name(classId, ((Id) obj).objectId);
            ret.add(new ECall3(t, new Var3(funcName), new Int3(params.size())));
            return ret;
        } else {
            callObjectId = ((Id) ((Access) obj).obj).objectId;
            params.add(0, new Param3(new Var3(callObjectId)));
            ret.addAll(params);
            callClassId = ((Id) ((Access) obj).obj).objectType;
            funcName = ClassDescriptor.lookup_flat_func_name(callClassId.getId(), ((Access) obj).fieldName);
            ret.add(new ECall3(t, new Var3(funcName), new Int3(params.size())));
        }
        return ret;
    }
}