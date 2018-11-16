package ir3;

public class ECall3 extends Instruction {
    public Arg3 arg2;

    public ECall3(Var3 result, Var3 funcName, VarList3 args) {
        super.op = Op3.CALL;
        super.arg1 = funcName;
        this.arg2 = args;
        super.result = result;
    }

    @Override
    public Arg3 getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result + " = " + super.arg1 + " " + arg2+ ";";
    }
}
