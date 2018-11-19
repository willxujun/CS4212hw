package ir3;

import java.util.ArrayList;

public class Bop3 extends Arg3 {
    public Op3 op;
    public Var3 arg1;
    public Var3 arg2;

    public Bop3(Op3 op, Var3 arg1, Var3 arg2) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String toString() {
        return arg1 + op.toString() + arg2;
    }

    @Override
    public ArrayList<Arg3> read() {
        ArrayList<Arg3> ls = new ArrayList<>();
        ls.add(arg1);
        ls.add(arg2);
        return ls;
    }
}
