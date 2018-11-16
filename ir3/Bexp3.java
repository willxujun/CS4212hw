package ir3;

public class Bexp3 extends Instruction {
    public Arg3 arg2;
    public Var3 result;

    public Bexp3(Var3 result, Arg3 arg1, Arg3 arg2, Op3 op3) {
        super.op = op3;
        super.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @Override
    public Var3 getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result + " = " +  arg1 + " " + super.op + " " + arg2+ ";";
    }
}
