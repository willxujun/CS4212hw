package ast;

import java.util.*;

import ir3.*;

public abstract class Expression extends Node {
    //abstract Value eval();

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return null;
    }

    public abstract Type typecheck(LocalEnvironment env);

    public abstract ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps);

    public boolean isDispatch() {return false;}
}

