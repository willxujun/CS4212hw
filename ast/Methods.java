package ast;

import java.util.ArrayList;

public class Methods extends Node {
    public ArrayList<Method> ms;

    public Methods() {
        ms = new ArrayList<Method>();
    }

    public Methods(Method m) {
        ms = new ArrayList<Method>();
        ms.add(m);
    }

    public Methods append(Method m) {
        this.ms.add(m);
        return this;
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(ms);
    }

    @Override
    public String toString() {
        return "MethodList";
    }
}
