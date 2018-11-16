package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Obj3;

public class Id extends Atom {
    public String objectId;
    public Type objectType;

    public Type callerClass;
    public String callerId;
    public String funcId;

    @Override
    public Type getCallerClass() {
        return callerClass;
    }

    @Override
    public String getCallerId() {
        return callerId;
    }

    @Override
    public String getFuncId() {
        return funcId;
    }

    public Id(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return Printable.parens("Id " + objectId);
    }

    @Override
    public boolean isId() {
        return true;
    }

    public Type typecheck(LocalEnvironment env) {
        Type t = env.lookup_var(objectId);
        if(t.isError()) {
            objectType = new Error_t("Unknown variable binding " + objectId, env.currMethod, env.currClass);
            objectType.printMsg();
            return objectType;
        }
        objectType = t;
        return objectType;
    }

    public Type typecheck_func(LocalEnvironment env) {
        Type t = env.lookup_func(objectId);
        if(t.isError()) {
            objectType = new Error_t("Unknown function binding " + objectId, env.currMethod, env.currClass);
            objectType.printMsg();
            return objectType;
        }
        //fill up function info
        callerId = "this";
        funcId = objectId;
        callerClass = env.lookup_var("this");
        objectType = t;
        return objectType;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ret.add(new Obj3(objectId));
        return ret;
    }
}
