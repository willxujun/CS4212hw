package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;

public class ExpParen extends Atom {
    public Expression exp;
    public Type type;

    public ExpParen(Expression exp) {
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
        return Printable.parens(exp.toString());
    }

    public Type typecheck(LocalEnvironment env) {
        type = exp.typecheck(env);
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = exp.genIR3(classId, temps);
        ret.addAll(code);
        return ret;
    }
}
