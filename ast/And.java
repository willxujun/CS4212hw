package ast;

import ir3.Op3;

public class And extends Op {
    public static String type = "Boolean";

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "And";
    }

    public Op3 getOp3() {
        return Op3.AND;
    }
}
