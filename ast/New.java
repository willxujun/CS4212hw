package ast;

import java.util.ArrayList;

import ir3.Assign3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.New3;

public class New extends Atom {
    public String typeId;
    public Type type;

    public New(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>();
    }
    @Override
    public String toString() {
        return "New " + typeId;
    }

    public Type typecheck(LocalEnvironment env) {
        if(ClassDescriptor.lookup(typeId) == null) {
            type = new Error_t("Constructor of unknown class " + typeId, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type = new Type(typeId);
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        Temp t = new Temp(typeId);
        temps.add(new Decl3(t));
        ret.add(new Assign3(t, new New3(typeId)));
        return ret;
    }
}
