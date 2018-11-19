package ir3;

public class Jmp3 extends Instruction {

    public Jmp3(Int3 labelNum) {
        super.op = Op3.GOTO;
        super.arg1 = labelNum;
    }

    @Override
    public Arg3 getResult() {
        return null;
    }
    @Override
    public Arg3 getArg1() {
        return null;
    }
}
