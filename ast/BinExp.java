package ast;

import java.util.ArrayList;

import ir3.Bexp3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Var3;

public class BinExp extends Expression {
    public Op op;
    public Expression l;
    public Expression r;
    public Type type;

    public BinExp(Op op, Expression l, Expression r) {
        this.op = op;
        this.l = l;
        this.r = r;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(l);
        ls.add(r);
        return ls;
    }
    @Override
    public String toString() {
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
            type = new Error_t("l expression does not have bool or int type in " + l + " " + op, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else if (!r_type.equals(new Type("Bool")) && !r_type.equals(new Type("Int"))){
            type = new Error_t("r expression does not have bool or int type in " + op + " " + r, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else if (!l_type.equals(r_type)){
            type = new Error_t("l expression has different type from r in " + l + op + r, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else if (!op.getType().equals("Rel") && !op.getType().equals("Arith")) {
            type = new Error_t("Invalid binary operator " + op, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        } else {
            type = new Error_t("Error in binary expression " + op, env.currMethod, env.currClass);
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
