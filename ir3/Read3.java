package ir3;

public class Read3 extends Instruction {
    public Read3(Var3 var) {
        super.op = Op3.READ;
        super.arg1 = var;
    }
}
