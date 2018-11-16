package ir3;

public class CJmp3 extends Instruction {
    public Arg3 arg2;

    public CJmp3(Arg3 test, Int3 labelNum) {
        super.op = Op3.IF;
        super.arg1 = test;
        this.arg2 = labelNum;
    }

    @Override
    public String toString() {
        return super.op + " "+ arg1 + " goto " + arg2+ ";";
    }
}
