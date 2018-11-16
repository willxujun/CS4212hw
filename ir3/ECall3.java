package ir3;

public class ECall3 extends Instruction {
    public Arg3 arg2;
    public Var3 result;

    public ECall3(Var3 result, Var3 funcName, VarList3 args) {
        super.op = Op3.CALL;
        super.arg1 = funcName;
        this.arg2 = args;
        this.result = result;
    }

    @Override
    public Var3 getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result + " = " + super.arg1 + " " + arg2+ ";";
    }
}
