package analysis;

import java.util.BitSet;

public abstract class Join {
    public abstract void join(DataflowValue a, DataflowValue b);

}

