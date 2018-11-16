package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Uexp3;
import ir3.Var3;

public class UExp extends Expression {
    public Op op;
    public Expression exp;
    public Type type;

    public UExp(Op op, Expression exp) {
        this.op = op;
        this.exp = exp;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(exp);
        return ls;
    }
    @Override
    public String toString() {
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
            type = new Error_t("Error in unary expression " + op + exp, env.currMethod, env.currClass);
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
