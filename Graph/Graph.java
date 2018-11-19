package Graph;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;
    public Node entry;
    public Node exit;

    public Graph() {
        nodes = new ArrayList<Node>();
    }

    public void reverse() {
        entry = exit;
        ArrayList<Node> tmp;
        for(Node n: nodes) {
            tmp = n.succ;
            n.succ = n.pred;
            n.pred = tmp;
        }
    }

}