package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Obj3;
import ir3.Str3;

public class Str extends Expression {
    public String val;

    public Str(String s) {
        val = s;
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return Printable.parens("Str " + Printable.parens(val));
    }

    public Type typecheck(LocalEnvironment env) {
        return new Type("String");
    }
    @Override
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3(new Str3(val)));
        return ret;
    }
}
