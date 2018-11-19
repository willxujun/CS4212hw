package Graph;

import java.util.ArrayList;

import analysis.DataflowValue;

public class Node {
    public ArrayList<Node> pred;
    public ArrayList<Node> succ;
    public DataflowValue in;
    public DataflowValue out;
    public boolean visited;

    public Node() {
        visited = false;
        pred = new ArrayList<Node>();
        succ = new ArrayList<Node>();
    }
}