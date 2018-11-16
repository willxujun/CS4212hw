package ir3;

import java.util.ArrayList;

import ast.Temp;

public class Decl3 extends Instruction {
    public Arg3 arg2;

    public Decl3(Var3 type, Var3 id) {
        super.op = Op3.DECL;
        super.arg1 = type;
        arg2 = id;
    }
    public Decl3(String type, String id) {
        super.op = Op3.DECL;
        super.arg1 = new Var3(type);
        arg2 = new Var3(id);
    }
    public Decl3(Temp t) {
        super.op = Op3.DECL;
        super.arg1 = new Var3(t.getType());
        arg2 = new Var3(t.toString());
    }

    public static void addDecl(ArrayList<Instruction> ls, Temp t) {
        ls.add(0, new Decl3(new Var3(t.getType()), new Var3(t.toString())));
    }

    public String toString() {
        return arg1 + " " + arg2;
    }
}
