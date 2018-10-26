import java.util.*;

abstract class Expression extends Node {
    //abstract Value eval();

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return null;
    }

    public abstract Type typecheck(LocalEnvironment env);

    public abstract ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps);

    public boolean isDispatch() {return false;}
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
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("Bool");
        temps.add(new Decl3(t));
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
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("Int");
        temps.add(new Decl3(t));
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
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp("Str");
        temps.add(new Decl3(t));
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

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> codeL = l.genIR3(classId, temps);
        ArrayList<Instruction> codeR = r.genIR3(classId, temps);
        Var3 resL = Instruction.getResultFromList(codeL);
        Var3 resR = Instruction.getResultFromList(codeR);

        Temp t = new Temp(type);
        temps.add(new Decl3(t));
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
        if(op.getType().equals("Arith") && exp_type.equals(new Type("Int"))) {
            type = new Type("Int");
            return type;
        }
        else if(op.getType().equals("Boolean") && exp_type.equals(new Type("Bool"))) {
            type = new Type("Bool");
            return type;
        }
        else {
            type = new Error_t("Error in unary expression", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId, temps);
        Var3 res = Instruction.getResultFromList(code);

        Temp t = new Temp(type);
        temps.add(new Decl3(t));
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

    public Type typecheck_func(LocalEnvironment env) {
        Type type = new Error_t("Should not reach here...", env.currMethod, env.currClass);
        type.printMsg();
        return type;
    }
    
    public String getCallerId() {
        return null;
    }
    public Type getCallerClass() {
        return null;
    }
    public String getFuncId() {
        return null;
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
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3("this"));
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
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3("null"));
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

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId, temps);
        Var3 res = Instruction.getResultFromList(code);

        Temp t = new Temp(type);
        temps.add(new Decl3(t));
        ret.addAll(code);
        ret.add(new Assign3(t,res));
        return ret;
    }
}

class Id extends Atom {
    public String objectId;
    public Type objectType;

    public Type callerClass;
    public String callerId;
    public String funcId;

    @java.lang.Override
    public Type getCallerClass() {
        return callerClass;
    }

    @java.lang.Override
    public String getCallerId() {
        return callerId;
    }

    @java.lang.Override
    public String getFuncId() {
        return funcId;
    }

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
        if(t.isError()) {
            objectType = new Error_t("Unknown variable binding " + objectId, env.currMethod, env.currClass);
            objectType.printMsg();
            return objectType;
        }
        objectType = t;
        return objectType;
    }

    public Type typecheck_func(LocalEnvironment env) {
        Type t = env.lookup_func(objectId);
        if(t.isError()) {
            objectType = new Error_t("Unknown function binding " + objectId, env.currMethod, env.currClass);
            objectType.printMsg();
            return objectType;
        }
        //fill up function info
        callerId = "this";
        funcId = objectId;
        callerClass = env.lookup_var("this");
        objectType = t;
        return objectType;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3(objectId));
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

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp(typeId);
        temps.add(new Decl3(t));
        ret.add(new Assign3(t, new New3(typeId)));
        return ret;
    }
}

class Access extends Atom {
    public Atom obj;
    public String fieldName;
    public Type type;

    public Type callerClass;
    public String callerId;
    public String funcId;

    public Access(Atom obj, String fieldName) {
        this.obj = obj;
        this.fieldName = fieldName;
    }

    @java.lang.Override
    public String getCallerId() {
        return callerId;
    }

    @java.lang.Override
    public Type getCallerClass() {
        return callerClass;
    }

    @java.lang.Override
    public String getFuncId() {
        return funcId;
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
        //left type cannot be Function_t
        Type obj_type = obj.typecheck(env);
        if(obj_type.isError()) {
            type = new Error_t("Access LHS does not type check", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        /*
        if(obj_type.isFunction()) {
            type = new Error_t("Access LHS cannot be function type");
            type.printMsg();
            return type;
        }
        if(obj_type.isTuple()) {
            obj_type = obj_type.fst();
        }
        */
        //overall type cannot be function
        Type var_type = ClassDescriptor.lookup_var(obj_type.getId(), fieldName);
        if(var_type.isError()) {
            type = new Error_t("Access RHS cannot find variable " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        this.type = var_type;
        return type;
    }

    public Type typecheck_func(LocalEnvironment env) {
        Type obj_type = obj.typecheck(env);
        if(obj_type.isError()) {
            type = new Error_t("Dispatch object does not type check for call to " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type func_type = ClassDescriptor.lookup_func(obj_type.getId(), fieldName);
        if(func_type.isError()) {
            type = new Error_t("Call to unknown function " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        this.type = func_type;
        //fill up function info
        callerClass = obj_type;
        funcId = fieldName;
        if(obj.isId()) {
            callerId = ((Id)obj).objectId;
        } else if(obj.isAccess()) {
            callerId = ((Access)obj).fieldName;
        } else {
            //nameless obj
            Temp t = new Temp(obj_type);
            callerId = t.toString();
        }
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = obj.genIR3(classId, temps);
        Var3 res = Instruction.getResultFromList(code);
        ret.addAll(code);

        Temp t = new Temp(type);
        if(!type.isFunction()) {
            temps.add(new Decl3(t));
            ret.add(new Assign3(t,
                    new Access3(res.getId(), fieldName)));
            return ret;
        } else {
            ret.add(new Obj3(callerId));
            return ret;
        }
    }
}

class Dispatch extends Atom {
    public Atom obj;
    public Expressions exps;
    public Type type;
    public String callClassId;
    public String funcId;

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

    @Override
    public boolean isDispatch() {
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Dispatch";
    }

    public Type typecheck(LocalEnvironment env) {
        //left type could be function or tuple type. Parse it to function type
        Type obj_type = obj.typecheck_func(env);
        //System.out.println(obj_type);
        if(!obj_type.isFunction()) {
            type = new Error_t("Calling a non-function type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }

        Type exps_type = exps.typecheck(env);
        if(exps_type.isError()) {
            type = new Error_t("Some function parameters do not type check", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }

        int res = ((Function_t)obj_type).match((Tuple_t)exps_type);
        switch (res) {
        case 0:
            type = new Type(((Function_t)obj_type).getReturnType());
            return type;
        case 1:
            type = new Error_t("Function call has wrong argument cardinality", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        case 2:
            type = new Error_t("Function call has wrong argument type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        default:
            System.out.println(res);
            type = new Error_t("Match function should not reach here");
            type.printMsg();
            return type;
        }

    }

    @Override
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> params = new ArrayList<Instruction>();
        ArrayList<Instruction> code = new ArrayList<Instruction>();
        Var3 res;
        VarList3 paramList = new VarList3();

        Type callerClass = obj.getCallerClass();
        String callerId = obj.getCallerId();
        String funcName = obj.getFuncId();

        code = obj.genIR3(classId, temps);
        res = Instruction.getResultFromList(code);
        ret.addAll(code);

        for (Expression e : exps.exps) {
            code = e.genIR3(classId, temps);
            ret.addAll(code);
            res = Instruction.getResultFromList(code);
            paramList.add(res);
        }

        Temp t = new Temp(type);
        temps.add(new Decl3(t));
        paramList.add(0, new Var3(callerId));
        funcName = ClassDescriptor.lookup_flat_func_name(callerClass.getId(), funcName);
        ret.add(new ECall3(t, new Var3(funcName), paramList));
        return ret;
    }
}