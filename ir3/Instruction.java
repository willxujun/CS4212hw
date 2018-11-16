package ir3;

import java.util.ArrayList;

public abstract class Instruction {
    public Op3 op;
    public Arg3 arg1;
    public Arg3 result;

    public static Arg3 getResultFromList(ArrayList<Instruction> ls) {
        Instruction last = ls.get(ls.size() - 1);
        if(last.isObj()) {
            ls.remove(ls.get(ls.size() - 1));
        }
        return last.getResult();
    }

    public static Instruction getLastInstruction(ArrayList<Instruction> ls) {
        if(ls.size() == 0)
            return null;
        else
            return ls.get(ls.size() - 1);
    }

    public static Instruction removeLastInstruction(ArrayList<Instruction> ls) {
        if(ls.size() == 0)
            return null;
        else
            return ls.remove(ls.size() - 1);
    }

    public Arg3 getResult() {
        return result;
    }

    public String toString() {
        return op + " " + arg1+ ";";
    }

    public boolean isObj() {
        return false;
    }
}