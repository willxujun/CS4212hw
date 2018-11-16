package ir3;

public class Assign3 extends Instruction {

    public Assign3(Var3 result, Arg3 val) {
        super.op = Op3.ASSIGN;
        super.arg1 = val;
        super.result = result;
    }

    public Assign3(Access3 result, Arg3 val) {
        super.op = Op3.ASSIGN;
        super.arg1 = val;
        super.result = result;
    }

    @Override
    public Arg3 getResult() {
        return super.result;
    }

    @Override
    public String toString() {
        return result + " " + super.op + " " + arg1 + ";";
    }
}
