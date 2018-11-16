package ast;

import java.util.ArrayList;

import ir3.Arg3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Print3;
import ir3.Var3;

public class PrintStmt extends Statement {
    public Expression exp;
    public Type type;

    public PrintStmt(Expression exp) {
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
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId, temps);
        Arg3 res = Instruction.getResultFromList(code);
        ret.addAll(code);
        ret.add(new Print3(res));
        return ret;
    }
}
