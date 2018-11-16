package ast;

import java.util.ArrayList;

import ir3.Arg3;
import ir3.Decl3;
import ir3.ECall3;
import ir3.Instruction;
import ir3.Var3;
import ir3.VarList3;

public class Dispatch extends Atom {
    public Atom obj;
    public Expressions exps;
    public Type type;
    public String callClassId;
    public String funcId;

    public Dispatch(Atom obj, Expressions exps) {
        this.obj = obj;
        this.exps = exps;
    }

    @Override
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

    @Override
    public String toString() {
        return "Dispatch";
    }

    public Type typecheck(LocalEnvironment env) {
        //left type could be function or tuple type. Parse it to function type
        Type obj_type = obj.typecheck_func(env);
        //System.out.println(obj_type);
        if(!obj_type.isFunction()) {
            type = new Error_t("Calling a non-function type " + obj_type, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }

        Type exps_type = exps.typecheck(env);
        if(exps_type.isError()) {
            type = new Error_t("Parameters do not type check in call to " + obj.getFuncId(), env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }

        int res = ((Function_t)obj_type).match((Tuple_t)exps_type);
        switch (res) {
        case 0:
            type = new Type(((Function_t)obj_type).getReturnType());
            return type;
        case 1:
            type = new Error_t("Function call has wrong argument cardinality in call to " + obj.getFuncId(), env.currMethod, env.currClass);
            type.printMsg();
            return type;
        case 2:
            type = new Error_t("Function call has wrong argument type in call to " + obj.getFuncId(), env.currMethod, env.currClass);
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
        ArrayList<Instruction> code = new ArrayList<Instruction>();
        Arg3 res;
        VarList3 paramList = new VarList3();

        code = obj.genIR3(classId, temps);
        Type callerClass = obj.getCallerClass();
        String callerId = obj.getCallerId();
        String funcName = obj.getFuncId();

        res = Instruction.getResultFromList(code);
        ret.addAll(code);

        for (Expression e : exps.exps) {
            code = e.genIR3(classId, temps);
            res = Instruction.getResultFromList(code);
            ret.addAll(code);
            paramList.add((Var3)res);
        }

        Temp t = new Temp(type);
        temps.add(new Decl3(t));
        paramList.add(0, new Var3(callerId));
        funcName = ClassDescriptor.lookup_flat_func_name(callerClass.getId(), funcName);
        ret.add(new ECall3(t, new Var3(funcName), paramList));
        return ret;
    }
}
