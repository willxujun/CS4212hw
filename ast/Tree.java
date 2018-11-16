package ast;

import java.util.*;

public class Tree {
    Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public String morePad(String pad) {
        return pad + "  ";
    }

    public String aux(Node n, String pad) {
        String ret = "";
        ret += n.toString() + '\n';
        List<Node> ls = n.getChildren();
        for(int i = 0; i<ls.size(); i++) {
            ret += pad;
            ret += aux(ls.get(i), morePad(pad));
        }
        return ret;
    }
    public String prettyPrint() {
        return aux(root, "  ");
    }

}