package ast;

import java.util.ArrayList;

public abstract class Atom extends Expression {
    @Override
    public ArrayList<Node> getChildren() {
        return null;
    }

    //recursion depth of atom is at most 2 for dispatch, use the following functions to force error for non-id type
    public boolean isId() {
        return false;
    }
    public boolean isAccess() {
        return false;
    }

    public Type typecheck_func(LocalEnvironment env) {
        Type type = new Error_t("Should not reach here...", env.currMethod, env.currClass);
        type.printMsg();
        return type;
    }

    public String getCallerId() {
        return null;
    }
    public Type getCallerClass() {
        return null;
    }
    public String getFuncId() {
        return null;
    }

}
