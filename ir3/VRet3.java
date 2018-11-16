package ir3;

public class VRet3 extends Instruction {
    public VRet3(Arg3 arg1) {
        super.op = Op3.VRET;
        super.arg1 = arg1;
    }
}
