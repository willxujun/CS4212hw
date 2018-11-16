package ir3;

import java.util.ArrayList;

public class VarList3 extends Arg3 {
    private ArrayList<Var3> varList;

    public VarList3() {
        varList = new ArrayList<Var3>();
    }
    public VarList3(ArrayList<Var3> varList) {
        this.varList = varList;
    }
    public void add(Var3 var) {
        varList.add(var);
    }
    public void add(int index, Var3 var) {
        varList.add(index, var);
    }
    public String toString() {
        return varList.toString();
    }
}
