package ast;

import ir3.Op3;

public class Plus extends Op {
    public static String type = "Arith";

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Plus";
    }

    public Op3 getOp3() {
        return Op3.ADD;
    }
}
