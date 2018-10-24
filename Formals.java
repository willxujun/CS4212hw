import java.util.*;

public class Formals extends Node {
    public ArrayList<Formal> formals;

    public Formals() {
        formals = new ArrayList<Formal>();
    }

    public Formals(Formal f) {
        formals = new ArrayList<Formal>();
        formals.add(f);
    }

    public Formals append(Formal f) {
        this.formals.add(f);
        return this;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(formals);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "FormalList";
    }
}