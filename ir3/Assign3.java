package ir3;

public class Assign3 extends Instruction {
    public Var3 result;

    public Assign3(Var3 result, Arg3 val) {
        super.op = Op3.ASSIGN;
        super.arg1 = val;
        this.result = result;
    }


    @Override
    public Var3 getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result + " " + super.op + " " + arg1 + ";";
    }
}
