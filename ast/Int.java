package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Int3;
import ir3.Obj3;

public class Int extends Expression {
    public int val;

    public Int(int a) {
        val = a;
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return Printable.parens("Int " + val);
    }

    public Type typecheck(LocalEnvironment env) {
        return new Type("Int");
    }
    @Override
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3(new Int3(val)));
        return ret;
    }
}
