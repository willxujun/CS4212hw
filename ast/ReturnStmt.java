package ast;

import java.util.ArrayList;
import java.util.Optional;

import ir3.Assign3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Ret3;
import ir3.VRet3;
import ir3.Var3;

public class ReturnStmt extends Statement {
    public Optional<Expression> exp;
    public Type type;

    public ReturnStmt() {
        this.exp = Optional.empty();
    }

    public ReturnStmt(Expression exp) {
        this.exp = Optional.of(exp);
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        if(exp.isPresent())
            ls.add(exp.get());
        return ls;
    }
    @Override
    public String toString() {
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
        if(exp.isPresent() && mtd_ret_type.equals(new Type("Void"))) {
            e = exp.get();
            ret_type = e.typecheck(env);
            type = new Error_t("Returning type " + ret_type + ", expected " + mtd_ret_type, env.currMethod, env.currClass);
        }
        if(!exp.isPresent() && !mtd_ret_type.equals(new Type("Void"))) {
            type = new Error_t("Returning type void, expected " + mtd_ret_type, env.currMethod, env.currClass);
        }
        type.printMsg();
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code;
        Var3 res;
        Expression e;
        Temp t;
        if(exp.isPresent()) {
            e = exp.get();
            code = e.genIR3(classId, temps);
            res = Instruction.getResultFromList(code);
            ret.addAll(code);
            if(res.isConst()) {
                t = new Temp(type);
                temps.add(new Decl3(t));
                ret.add(new Assign3(t, res));
                ret.add(new VRet3(t));
            } else {
                ret.add(new VRet3(res));
            }
        } else {
            ret.add(new Ret3());
        }
        return ret;
    }

}
