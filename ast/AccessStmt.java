package ast;

import java.util.ArrayList;

import ir3.Access3;
import ir3.Assign3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Var3;

//field assignment
public class AccessStmt extends Statement {
    public Atom obj;
    public String fieldName;
    public Expression exp;
    public Type type;

    public AccessStmt(Atom obj, String fieldName, Expression exp) {
        this.obj = obj;
        this.fieldName = fieldName;
        this.exp = exp;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(obj);
        ls.add(exp);
        return ls;
    }

    @Override
    public String toString() {
        return "Assign " + fieldName;
    }

    public Type typecheck(LocalEnvironment env) {
        Type obj_type = obj.typecheck(env);
        if (obj_type.isError()) {
            type = new Error_t("field assign: LHS of l value does not type check for field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type field_type = ClassDescriptor.lookup_var(obj_type.getId(), fieldName);
        if (field_type.isError()) {
            type = new Error_t("field assign: RHS of l value does not type check for field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type exp_type = exp.typecheck(env);
        if (exp_type.isError()) {
            type = new Error_t("field assign: r value does not type check for field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        if (!exp_type.equals(field_type)) {
            type = new Error_t("Error: type mismatch, assigning " + exp_type + " to " + field_type + " for field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        this.type = exp_type;
        return new Type("Void");
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = obj.genIR3(classId, temps);
        Var3 lValue = Instruction.getResultFromList(code);
        Var3 rValue;
        ret.addAll(code);

        code = exp.genIR3(classId, temps);
        rValue = Instruction.getResultFromList(code);
        ret.addAll(code);

        Temp t = new Temp(type);
        temps.add(new Decl3(t));
        ret.add(new Assign3(t, new Access3(lValue.getId(), fieldName)));
        ret.add(new Assign3(t, rValue));
        return ret;
    }
}
