package ir3;

public class VRet3 extends Instruction {
    public VRet3(Arg3 arg1) {
        super.op = Op3.VRET;
        super.arg1 = arg1;
    }

    @Override
    public Arg3 getResult() {
        return null;
    }

    @Override
    public Arg3 getArg1() {
        return arg1;
    }
}
