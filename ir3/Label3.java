package ir3;

public class Label3 extends Instruction {
    private static int num = 0;

    private int n;
    public Label3() {
        super.op = Op3.LABEL;
        this.n = num;
        super.arg1 = new Int3(this.n);
        num++;
    }
    public int getN() {
        return n;
    }

    @Override
    public Arg3 getResult() {
        return null;
    }
    @Override
    public Arg3 getArg1() {
        return null;
    }

    public String toString() {
        return super.op.toString() + arg1 + ":";
    }
}
