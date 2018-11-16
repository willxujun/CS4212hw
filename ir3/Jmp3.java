package ir3;

public class Jmp3 extends Instruction {

    public Jmp3(Int3 labelNum) {
        super.op = Op3.GOTO;
        super.arg1 = labelNum;
    }
}
