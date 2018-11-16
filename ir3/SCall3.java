package ir3;

//call, discarding result
public class SCall3 extends Instruction {
    public Arg3 arg2;

    public SCall3(Var3 funcName, VarList3 args) {
        super.op = Op3.CALL;
        super.arg1 = funcName;
        this.arg2 = args;
    }

    @Override
    public String toString() {
        return super.arg1 + "" + arg2 + ";";
    }
}
