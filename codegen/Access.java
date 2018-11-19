package codegen;

import java.util.ArrayList;

public abstract class Access {
    public void addSpill(Integer offset) {}

    public void addFieldAccess(Access access) {}

    public void setOp(MemOp op) {}
}

