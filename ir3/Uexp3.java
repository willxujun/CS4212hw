package ir3;

public class Uexp3 extends Instruction {
    public Var3 result;

    public Uexp3(Var3 result, Arg3 arg1, Op3 op3) {
        super.arg1 = arg1;
        super.op = op3;
        this.result = result;
    }

    @Override
    public Var3 getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result + " = " + super.op + " " + arg1+ ";";
    }
}
