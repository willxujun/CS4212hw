package ir3;

//call, storing result
public class Param3 extends Instruction {
    public Param3(Arg3 arg1) {
        super.op = Op3.PARAM;
        super.arg1 = arg1;
    }
}
