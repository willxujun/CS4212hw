
abstract class Op {
    public abstract String getType();

    public abstract Op3 getOp3();
}
class Plus extends Op {
    public static String type = "Arith";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Plus";
    }

    public Op3 getOp3() {
        return Op3.ADD;
    }
}
class Minus extends Op {
    public static String type = "Arith";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Minus";
    }

    public Op3 getOp3() {
        return Op3.SUB;
    }
}
class Mult extends Op {
    public static String type = "Arith";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Mult";
    }

    public Op3 getOp3() {
        return Op3.MULT;
    }
}
class Div extends Op {
    public static String type = "Arith";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Div";
    }

    public Op3 getOp3() {
        return Op3.DIV;
    }
}
class Neg extends Op {
    public static String type = "Arith";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Neg";
    }

    public Op3 getOp3() {
        return Op3.NEG;
    }
}
class Or extends Op {
    public static String type = "Boolean";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Or";
    }

    public Op3 getOp3() {
        return Op3.OR;
    }
}
class And extends Op {
    public static String type = "Boolean";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "And";
    }

    public Op3 getOp3() {
        return Op3.AND;
    }
}
class Not extends Op {
    public static String type = "Boolean";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Not";
    }

    public Op3 getOp3() {
        return Op3.NOT;
    }
}
class LT extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "LT";
    }

    public Op3 getOp3() {
        return Op3.LT;
    }
}
class GT extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "GT";
    }

    public Op3 getOp3() {
        return Op3.GT;
    }
}
class LE extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "LE";
    }

    public Op3 getOp3() {
        return Op3.LE;
    }
}
class GE extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "GE";
    }

    public Op3 getOp3() {
        return Op3.GE;
    }
}
class EQ extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "EQ";
    }

    public Op3 getOp3() {
        return Op3.EQ;
    }
}
class NE extends Op {
    public static String type = "Rel";

    public String getType() {
        return type;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "NE";
    }

    public Op3 getOp3() {
        return Op3.NE;
    }
}
