package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Obj3;

public class This extends Atom {
    public Type type;

    public This() {
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return "This";
    }

    public Type typecheck(LocalEnvironment env) {
        type = env.lookup_var("this");
        return type;
    }

    @Override
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3("this"));
        return ret;
    }
}
