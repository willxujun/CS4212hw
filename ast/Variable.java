package ast;

import java.util.*;

public class Variable extends Node {
    public String type;
    public String id;

    public Variable(String type, String id) {
        this.type = type;
        this.id = id;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return type + " " + id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}