package ast;

import java.util.ArrayList;

import ir3.Arg3;
import ir3.Assign3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.New3;
import ir3.Var3;

public class AssignStmt extends Statement {
    public String id;
    public Expression value;
    public Type type;

    public AssignStmt(String id, Expression value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(value);
        return ls;
    }
    @Override
    public String toString() {
        return "Assign " + id;
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_exp = value.typecheck(env);
        Type type_id = env.lookup_var(id);
        if(type_id.isError()){
            type = new Error_t("Unknown variable binding " + id, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        if(type_exp.isError()) {
            type = new Error_t("r value does not type check in assignment to " + id, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        if(type_exp.equals(type_id)) {
            type = new Type("Void");
            return type;
        } else {
            type = new Error_t("Assigning type " + type_exp + " to " + id + " of type " + type_id, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
    }
    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = value.genIR3(classId, temps);

        Instruction last = Instruction.getLastInstruction(code);

        Arg3 res = Instruction.getResultFromList(code);
        if(last.arg1 instanceof New3) {
            Instruction.removeLastInstruction(code);
            ret.addAll(code);
            ret.add(new Assign3(new Var3(id), last.arg1));
            return ret;
        } else {
            ret.addAll(code);
            ret.add(new Assign3(new Var3(id), res));
            return ret;
        }
    }
}
