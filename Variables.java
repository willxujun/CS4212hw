import java.util.*;

public class Variables extends Node{
    public ArrayList<Variable> vars;

    public Variables() {
        vars = new ArrayList<Variable>();
    }

    public Variables(Variable v) {
        vars = new ArrayList<Variable>();
        vars.add(v);
    }

    public Variables append(Variable v) {
        this.vars.add(v);
        return this;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(vars);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "VariableList";
    }
}