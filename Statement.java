import java.util.*;

public abstract class Statement extends Node{
    //abstract void execute();

    @java.lang.Override
    public abstract ArrayList<Node> getChildren();

    public abstract Type typecheck(LocalEnvironment env);

    public abstract ArrayList<Instruction> genIR3(String classId);
}