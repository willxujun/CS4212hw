import java.util.*;

class Printable {
    public static String pad = "  ";
    public static String listToString(List<?> ls) {
        String result = "";
        for(int i=0; i<ls.size(); i++) {
            result += pad + ls.get(i).toString() + '\n';
        }
        return result;
    }

    public static String parens(String s) {
        return "(" + s + ")";
    }
}

class Err extends MyClass {
    String msg;

    public Err(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return true;
    }
    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return msg;
    }
}

public class Program extends Node {
    public static boolean hasParseError = false;
    public MyClasses classes;

    public Program(MyClasses classes) {
        hasParseError = MyClasses.hasParseError;
        this.classes = classes;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(classes.classes);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Program";
    }

    public boolean isOK() {
        LocalEnvironment env = new LocalEnvironment();
        boolean res = true;
        ArrayList<MyClass> ls = classes.classes;
        MyClass cl;
        for (int i=0; i<ls.size(); i++) {
            cl = ls.get(i);
            res = res && cl.isOK(env);
        }
        return res;
    }

    public ArrayList<MethodData> genIR3() {
        ArrayList<MethodData> ret = new ArrayList<MethodData>();
        MethodData curr;
        ArrayList<Instruction> code;

        ClassDescriptor.flattenFuncNames();
        for(MyClass cl: classes.classes) {
            for(Method mtd: cl.ms.ms) {
                code = mtd.genIR3(cl.id);
                mtd.formals.formals.add(0, new Formal(cl.id, "this"));
                //format mtd.formals, generate temporaries for mtd.vars
                curr = new MethodData(cl.id, mtd.id, mtd.type, mtd.formals.formals, mtd.vars.vars, code);
                ret.add(curr);
            }
        }
        return ret;
    }
}

class MyClasses {
    public static boolean hasParseError = false;
    public ArrayList<MyClass> classes;

    public MyClasses() {
        classes = new ArrayList<MyClass>();
    }

    public MyClasses append(MyClass e) {
        this.classes.add(e);
        if(e.isError()) {
            hasParseError = true;
        }
        return this;
    }

    public MyClasses append(int i, MyClass e) {
        if(e.isError()) {
            hasParseError = true;
        }
        this.classes.add(i, e);
        return this;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "ClassList";
    }
}

class MyClass extends Node {
    public String id;
    public Variables vs;
    public Methods ms;

    public MyClass() {
    }

    public MyClass(String id, Variables vs, Methods ms) {
        this.id = id;
        this.vs = vs;
        this.ms = ms;

        //put inside class descriptor and do distinct name check
    }

    public boolean isError() {
        return false;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(vs);
        ls.add(ms);
        return ls;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Class " + id;
    }

    public boolean isOK(LocalEnvironment env) {
        LocalEnvironment newEnv = env.augment(this.id);
        boolean res = true;
        ArrayList<Method> mList = ms.ms;
        Method m;
        for (int i=0; i<mList.size(); i++) {
            m = mList.get(i);
            res = res && m.isOK(newEnv);
        }
        return res;
    }
}

class Vms {
    public Variables vs;
    public Methods ms;

    public Vms() {
        vs = new Variables();
        ms = new Methods();
    }

    public Vms(Variables vs) {
        this.vs = vs;
        this.ms = new Methods();
    }

    public Vms(Methods ms) {
        this.vs = new Variables();
        this.ms = ms;
    }

    public Vms(Variables vs, Methods ms) {
        this.vs = vs;
        this.ms = ms;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Vms{" +
                "vs=" + vs +
                ", ms=" + ms +
                '}';
    }
}

class Methods extends Node {
    public ArrayList<Method> ms;

    public Methods() {
        ms = new ArrayList<Method>();
    }

    public Methods(Method m) {
        ms = new ArrayList<Method>();
        ms.add(m);
    }

    public Methods append(Method m) {
        this.ms.add(m);
        return this;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(ms);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "MethodList";
    }
}

abstract class Value {

}

class IfStmt extends Statement {
    public Expression test;
    public Statements codeTrue;
    public Statements codeFalse;
    public Type type;

    public IfStmt(Expression test, Statements codeTrue, Statements codeFalse) {
        this.test = test;
        this.codeTrue = codeTrue;
        this.codeFalse = codeFalse;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(test);
        ls.add(codeTrue);
        ls.add(codeFalse);
        return ls;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "If";
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_test, type_taken, type_untaken;
        type_test = test.typecheck(env);
        if(!type_test.equals(new Type("Bool"))) {
            type = new Error_t("If statement has non-bool test type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type_taken = codeTrue.typecheck(env);
        type_untaken = codeFalse.typecheck(env);
        if(!type_taken.equals(type_untaken)) {
            type = new Error_t("If statement has different branch types", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type = type_taken;
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> condTest = new ArrayList<Instruction>();
        ArrayList<Instruction> codeTrue = new ArrayList<Instruction>();
        ArrayList<Instruction> codeFalse = new ArrayList<Instruction>();

        condTest = test.genIR3(classId);
        for(Statement s: this.codeTrue.statements) {
            codeTrue.addAll(s.genIR3(classId));
        }
        for(Statement s: this.codeFalse.statements) {
            codeFalse.addAll(s.genIR3(classId));
        }
        //connect lTrue with codeTrue and lFalse with codeFalse
        //condTest[n]; cJmp condRes lTrue; ucJmp lFalse; lTrue; codeTrue; lFalse; codeFalse;
        Label3 lTrue = new Label3();
        Label3 lFalse = new Label3();
        Var3 condRes = Instruction.getResultFromList(condTest);
        CJmp3 cJmp = new CJmp3(condRes, new Int3(lTrue.getN()));
        Jmp3 ucJmp = new Jmp3(new Int3(lFalse.getN()));

        ret.addAll(condTest);
        ret.add(cJmp);
        ret.add(ucJmp);
        ret.add(lTrue);
        ret.addAll(codeTrue);
        ret.add(lFalse);
        ret.addAll(codeFalse);

        return ret;
    }
}

class WhileStmt extends Statement {
    public Expression test;
    public Statements loop;
    public Type type;

    public WhileStmt(Expression test, Statements loop) {
        this.test = test;
        this.loop = loop;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(test);
        ls.add(loop);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "While";
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_test, type_loop;
        type_test = test.typecheck(env);
        if(!type_test.equals(new Type("Bool"))) {
            type = new Error_t("While statement has non-bool test type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type = loop.typecheck(env);
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> condTest = new ArrayList<Instruction>();
        ArrayList<Instruction> codeLoop = new ArrayList<Instruction>();

        condTest = test.genIR3(classId);
        for(Statement s: loop.statements) {
            codeLoop.addAll(s.genIR3(classId));
        }
        //connect lTrue with codeTrue and lFalse with codeFalse
        //condTest[n]; cJmp condRes lTrue; ucJmp lFalse; lTrue; codeTrue; lFalse; codeFalse;
        Label3 lLoop = new Label3();
        Label3 lTrue = new Label3();
        Label3 lFalse = new Label3();

        Var3 condRes = Instruction.getResultFromList(condTest);
        CJmp3 cJmp = new CJmp3(condRes, new Int3(lTrue.getN()));
        Jmp3 ucJmp = new Jmp3(new Int3(lFalse.getN()));
        Jmp3 ucJmp_loop = new Jmp3(new Int3(lLoop.getN()));

        ret.add(lLoop);
        ret.addAll(condTest);
        ret.add(cJmp);
        ret.add(ucJmp);
        ret.add(lTrue);
        ret.addAll(codeLoop);
        ret.add(ucJmp_loop);
        ret.add(lFalse);

        return ret;
    }
}

class ReadStmt extends Statement {
    public String intoId;
    public Type type;

    public ReadStmt(String intoId) {
        this.intoId = intoId;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "Read " + intoId;
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_id = env.lookup_var(intoId);
        if(type_id != null && (type_id.equals(new Type("Int")) || type_id.equals(new Type("Bool")) || type_id.equals(new Type("String")))) {
            type = new Type("Void");
            return type;
        }
        else if (type_id == null) {
            type = new Error_t("Unknown variable binding " + intoId, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else {
            type = new Error_t("Reading into variable of the wrong type. Correct type is: Int | String | Bool", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Read3(new Var3(intoId)));
        return ret;
    }
}

class PrintStmt extends Statement {
    public Expression exp;
    public Type type;

    public PrintStmt(Expression exp) {
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
        return "Print";
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_exp = exp.typecheck(env);
        if(type_exp.equals(new Type("Int")) || type_exp.equals(new Type("Bool")) || type_exp.equals(new Type("String"))) {
            type = new Type("Void");
            return type;
        } else {
            type = new Error_t("Printing expression of type " + type_exp, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId);
        Var3 res = Instruction.getResultFromList(code);
        ret.addAll(code);
        ret.add(new Print3(res));
        return ret;
    }
}

class AssignStmt extends Statement {
    public String id;
    public Expression value;
    public Type type;

    public AssignStmt(String id, Expression value) {
        this.id = id;
        this.value = value;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(value);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "Assign " + id;
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_exp = value.typecheck(env);
        Type type_id = env.lookup_var(id);
        if(type_id != null && type_exp.equals(type_id)) {
            type = new Type("Void");
            return type;
        }
        else if(type_id == null){
            type = new Error_t("Unknown variable binding " + id, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else {
            type = new Error_t("Assigning type " + type_exp + " to " + id + ": " + type_id, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = value.genIR3(classId);
        Var3 res = Instruction.getResultFromList(code);
        ret.addAll(code);
        ret.add(new Assign3(new Var3(id), res));
        return ret;
    }
}

class AccessStmt extends Statement {
    public Atom obj;
    public String fieldName;
    public Expression exp;
    public Type type;

    public AccessStmt(Atom obj, String fieldName, Expression exp) {
        this.obj = obj;
        this.fieldName = fieldName;
        this.exp = exp;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(obj);
        ls.add(exp);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "Assign " + fieldName;
    }

    public Type typecheck(LocalEnvironment env) {
        if(!obj.isId()) {
            type = new Error_t("Accessing field of a non-object", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type type_atom = env.lookup_var(((Id)obj).objectId);
        if(type_atom == null) {
            type = new Error_t("Unknown variable binding " + ((Id)obj).objectId, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type type_exp = exp.typecheck(env);
        Type type_fName = ClassDescriptor.lookup_field_type(type_atom.getId(), fieldName);
        if(type_fName == null) {
            type = new Error_t("Variable " + ((Id)obj).objectId + " has no field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        if(!type_fName.equals(type_exp)) {
            type = new Error_t("Error: type mismatch, assigning " + type_exp + " to " + type_fName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        this.type = type_exp;
        return new Type("Void");
    }
    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId);
        Var3 res = Instruction.getResultFromList(code);

        Temp t = new Temp(type);
        Decl3.addDecl(ret, t);
        ret.add(new Assign3(t, new Access3(((Id)obj).objectId, fieldName)));
        ret.add(new Assign3(t, res));
        return ret;
    }
}

class DispatchStmt extends Statement {
    public Atom obj;
    public Expressions exps;
    public Type type;

    public DispatchStmt(Atom obj, Expressions exps) {
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
        if(obj.isId()) {
            mName = ((Id)obj).objectId;
            methodSig = env.lookup_mtd(mName);
            if(methodSig != null) {
                formalMap = methodSig.x;
                res = exps.check_exps(formalMap, env);
                if(res == 0) {
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
        if(obj.isAccess() && ((Access)obj).obj.isId()) {
            object_name = ((Access)obj).fieldName;
            object_type = ((Id)((Access)obj).obj).typecheck(env);
            if(object_type == null) {
                type = new Error_t("Unknown variable binding " + object_name, env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
            class_info = ClassDescriptor.lookup(object_type.getId());
            if(class_info == null) {
                type = new Error_t("Unknown class " + object_type, env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
            mName = ((Id)
                    ((Access)obj).obj).objectId;
            methodSig = ClassDescriptor.lookup_method_sig(object_type.getId(), mName);
            formalMap = methodSig.x;
            res = exps.check_exps(formalMap, env);
            if(res == 0) {
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
        String callClassId;
        String funcName;

        for(Expression e: exps.exps) {
            code = e.genIR3(classId);
            ret.addAll(code);
            res = Instruction.getResultFromList(code);
            params.add(new Param3(res));
        }
        ret.addAll(params);
        if(obj.isId()) {
            funcName = ClassDescriptor.lookup_flat_func_name(classId, ((Id)obj).objectId);
            ret.add(new SCall3(new Var3(funcName), new Int3(params.size())));
            return ret;
        }
        else {
            callClassId = ((Id)((Access)obj).obj).objectType.getId();
            funcName = ClassDescriptor.lookup_flat_func_name(callClassId, ((Access)obj).fieldName);
            ret.add(new SCall3(new Var3(funcName), new Int3(params.size())));
        }
        return ret;
    }
}

class ReturnStmt extends Statement {
    public Optional<Expression> exp;
    public Type type;

    public ReturnStmt() {
        this.exp = Optional.empty();
    }

    public ReturnStmt(Expression exp) {
        this.exp = Optional.of(exp);
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        if(exp.isPresent())
            ls.add(exp.get());
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "Return";
    }

    public Type typecheck(LocalEnvironment env) {
        Type ret_type;
        Expression e;
        Type mtd_ret_type = env.lookup_ret_type();
        if(mtd_ret_type.equals(new Type("Void")) && !exp.isPresent()) {
            type = new Type("Void");
            return type;
        }
        if(exp.isPresent() && !mtd_ret_type.equals(new Type("Void"))) {
            e = exp.get();
            ret_type = e.typecheck(env);
            if (ret_type.equals(mtd_ret_type)) {
                type = ret_type;
                return ret_type;
            } else {
                type = new Error_t("returning " + ret_type + ", " + mtd_ret_type + " expected", env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
        }
        type = new Error_t("Return statement does not have appropriate type", env.currMethod, env.currClass);
        type.printMsg();
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code;
        Var3 res;
        Expression e;
        if(exp.isPresent()) {
            e = exp.get();
            code = e.genIR3(classId);
            res = Instruction.getResultFromList(code);
            ret.addAll(code);
            ret.add(new VRet3(res));
        } else {
            ret.add(new Ret3());
        }
        return ret;
    }

}