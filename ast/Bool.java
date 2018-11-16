package ast;

import java.util.ArrayList;

import ir3.Bool3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Obj3;

public class Bool extends Expression {
    public boolean val;

    public Bool(boolean b) {
        val = b;
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return Printable.parens("Bool " + val);
    }

    public Type typecheck(LocalEnvironment env) {
        return new Type("Bool");
    }

    @Override
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3(new Bool3(val)));
        return ret;
    }
}
