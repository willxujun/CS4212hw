package ast;

import java.util.ArrayList;

public class Err extends MyClass {
    String msg;

    public Err(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return true;
    }
    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }

    @Override
    public String toString() {
        return msg;
    }
}
