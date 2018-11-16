package ast;

import ir3.Op3;

public class Not extends Op {
    public static String type = "Boolean";

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Not";
    }

    public Op3 getOp3() {
        return Op3.NOT;
    }
}
