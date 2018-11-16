package ast;

import java.util.ArrayList;

import ir3.Arg3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.SCall3;
import ir3.Var3;
import ir3.VarList3;

public class DispatchStmt extends Statement {
    public Atom obj;
    public Expressions exps;
    public Type type;

    public DispatchStmt(Atom obj, Expressions exps) {
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
    public String toString() {
        return "DispatchStmt";
    }

    public Type typecheck(LocalEnvironment env) {
        Type obj_type = obj.typecheck_func(env);
        if(!obj_type.isFunction()) {
            type = new Error_t("Calling a non-function type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }

        Type exps_type = exps.typecheck(env);
        if(exps_type.isError()) {
            type = new Error_t("Some function parameters do not type check for function " + obj.getFuncId(), env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }

        int res = ((Function_t)obj_type).match((Tuple_t)exps_type);
        switch (res) {
        case 0:
            type = new Type(((Function_t)obj_type).getReturnType());
            return type;
        case 1:
            type = new Error_t("Function call has wrong argument cardinality for function " + obj.getFuncId(), env.currMethod, env.currClass);
            type.printMsg();
            return type;
        case 2:
            type = new Error_t("Function call has wrong argument type", env.currMethod, env.currClass);
            type.printMsg();
            return type;
        default:
            type = new Error_t("Match function should not reach here " + "atom type is " + obj_type.getClass().getName() + " exp type is " + exps_type.getClass().getName());
            type.printMsg();
            return type;
        }
    }

    @Override
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> params = new ArrayList<Instruction>();
        ArrayList<Instruction> code = new ArrayList<Instruction>();
        Arg3 res;
        VarList3 paramList = new VarList3();

        Type callerClass = obj.getCallerClass();
        String callerId = obj.getCallerId();
        String funcName = obj.getFuncId();

        code = obj.genIR3(classId, temps);
        res = Instruction.getResultFromList(code);
        ret.addAll(code);

        for (Expression e : exps.exps) {
            code = e.genIR3(classId, temps);
            res = Instruction.getResultFromList(code);
            ret.addAll(code);
            paramList.add((Var3)res);
        }

        paramList.add(0, new Var3(callerId));
        funcName = ClassDescriptor.lookup_flat_func_name(callerClass.getId(), funcName);
        ret.add(new SCall3(new Var3(funcName), paramList));
        return ret;
    }
}
