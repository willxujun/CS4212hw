package analysis;

import java.util.BitSet;
import java.util.LinkedList;

import Graph.*;

public abstract class DataflowAnalyzer {
    public boolean isBackward;
    public Join op;
    public TransferFunction function;
    public Graph graph;

    public void run() {
        if(isBackward)
            graph.reverse();
        bfs(graph.entry);
    }

    private void bfs(Node entry) {
        LinkedList<Node> q = new LinkedList<Node>();
        DataflowValue tmp;
        boolean hasChange = true;
        q.add(entry);
        Node n;
        while(hasChange) {
            while (!q.isEmpty()) {
                n = q.poll();
                n.visited = true;
                if (isBackward) {
                    for (Node x : n.pred) {
                        tmp = n.out.clone();
                        op.join(n.out, x.in);
                        if(!tmp.equals(n.out))
                            hasChange = true;
                    }
                    tmp = n.in.clone();
                    function.f(n.in, n.out);
                    if(!tmp.equals(n.in))
                        hasChange = true;
                } else {
                    for (Node x : n.pred) {
                        tmp = n.in.clone();
                        op.join(n.in, x.out);
                        if(!tmp.equals(n.in))
                            hasChange = true;
                    }
                    tmp = n.out.clone();
                    function.f(n.out, n.in);
                    if(!tmp.equals(n.out))
                        hasChange = true;
                }

                for (Node x : n.succ) {
                    if (!x.visited)
                        q.add(x);
                }
            }
        }
    }
}
