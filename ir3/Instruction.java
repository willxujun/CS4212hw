package ir3;

import java.util.ArrayList;

public abstract class Instruction {
    Op3 op;
    Arg3 arg1;

    public static Var3 getResultFromList(ArrayList<Instruction> ls) {
        Instruction last = ls.get(ls.size() - 1);
        if(last.isObj()) {
            ls.remove(ls.get(ls.size() - 1));
        }
        return last.getResult();
    }

    public Var3 getResult() {
        return null;
    }

    public String toString() {
        return op + " " + arg1+ ";";
    }

    public boolean isObj() {
        return false;
    }
}