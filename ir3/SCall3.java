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
    public Arg3 getArg1() {
        return null;
    }

    @Override
    public Arg3 getArg2() {
        return arg2;
    }

    @Override
    public Arg3 getResult() {
        return null;
    }

    @Override
    public String toString() {
        return super.arg1 + "" + arg2 + ";";
    }
}
