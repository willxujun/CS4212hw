package ir3;

public class Print3 extends Instruction {
    public Print3(Arg3 arg1) {
        super.op = Op3.PRINT;
        super.arg1 = arg1;
    }
}
