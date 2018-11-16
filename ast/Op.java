package ast;

import ir3.*;

public abstract class Op {
    public abstract String getType();

    public abstract Op3 getOp3();
}
