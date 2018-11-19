package analysis;

import java.util.BitSet;
import Graph.*;

public class LivenessAnalyzer extends DataflowAnalyzer {
    public LivenessAnalyzer(Graph g) {
        isBackward = true;
        op = new Or();
        function = new F();
        graph = g;
    }
}

class BitVec extends DataflowValue {
    public BitSet vec;

    public BitVec(int size) {
        vec = new BitSet(size);
    }

    public BitVec(BitSet s) {
        vec = s;
    }

    @Override
    public DataflowValue clone() {
        return new BitVec((BitSet)(vec.clone()));
    }

    @Override
    public void or(DataflowValue b) {
        if(b instanceof BitVec) {
            vec.or(((BitVec) b).vec);
        }
    }
}

class Or extends Join {
    public Or() {}

    public void join(DataflowValue a, DataflowValue b) {
        a.or(b);
    }
}

class F extends TransferFunction {
    public F() {}

    public void f(DataflowValue out, DataflowValue in) {
        BitVec a = (BitVec)out;
        BitVec b = (BitVec)in;

    }
}