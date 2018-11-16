package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Obj3;

public class Null extends Atom {
    public Null() {
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return "Null";
    }

    public Type typecheck(LocalEnvironment env) {
        return new Null_t();
    }
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3("null"));
        return ret;
    }
}
