package ast;

import ir3.Op3;

public class LE extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }
    @Override
    public String toString() {
        return "LE";
    }

    public Op3 getOp3() {
        return Op3.LE;
    }
}
