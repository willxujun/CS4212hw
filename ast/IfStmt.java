package ast;

import java.util.ArrayList;

import ir3.Arg3;
import ir3.CJmp3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Int3;
import ir3.Jmp3;
import ir3.Label3;
import ir3.Var3;

public class IfStmt extends Statement {
    public Expression test;
    public Statements codeTrue;
    public Statements codeFalse;
    public Type type;

    public IfStmt(Expression test, Statements codeTrue, Statements codeFalse) {
        this.test = test;
        this.codeTrue = codeTrue;
        this.codeFalse = codeFalse;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(test);
        ls.add(codeTrue);
        ls.add(codeFalse);
        return ls;
    }

    @Override
    public String toString() {
        return "If";
    }

    public Type typecheck(LocalEnvironment env) {
        Type type_test, type_taken, type_untaken;
        type_test = test.typecheck(env);
        if(!type_test.equals(new Type("Bool"))) {
            type = new Error_t("If statement has non-bool test type " + type_test, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type_taken = codeTrue.typecheck(env);
        type_untaken = codeFalse.typecheck(env);
        if(!type_taken.equals(type_untaken)) {
            type = new Error_t("If statement has different branch types " + type_taken + ", " + type_untaken, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        type = type_taken;
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> condTest = new ArrayList<Instruction>();
        ArrayList<Instruction> codeTrue = new ArrayList<Instruction>();
        ArrayList<Instruction> codeFalse = new ArrayList<Instruction>();

        condTest = test.genIR3(classId, temps);
        for(Statement s: this.codeTrue.statements) {
            codeTrue.addAll(s.genIR3(classId, temps));
        }
        for(Statement s: this.codeFalse.statements) {
            codeFalse.addAll(s.genIR3(classId, temps));
        }
        //connect lTrue with codeTrue and lFalse with codeFalse
        //condTest[n]; cJmp condRes lTrue; ucJmp lFalse; lTrue; codeTrue; lFalse; codeFalse;
        Label3 lTrue = new Label3();
        Label3 lFalse = new Label3();
        Arg3 condRes = Instruction.getResultFromList(condTest);
        CJmp3 cJmp = new CJmp3(condRes, new Int3(lTrue.getN()));
        Jmp3 ucJmp = new Jmp3(new Int3(lFalse.getN()));

        ret.addAll(condTest);
        ret.add(cJmp);
        ret.add(ucJmp);
        ret.add(lTrue);
        ret.addAll(codeTrue);
        ret.add(lFalse);
        ret.addAll(codeFalse);

        return ret;
    }
}
