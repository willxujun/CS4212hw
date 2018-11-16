package ir3;

public class Ret3 extends Instruction {
    public Ret3() {
        super.op = Op3.RET;
    }

    @Override
    public String toString() {
        return super.op + ";";
    }
}
