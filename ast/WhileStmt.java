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

public class WhileStmt extends Statement {
        public Expression test;
        public Statements loop;
        public Type type;

        public WhileStmt(Expression test, Statements loop) {
            this.test = test;
            this.loop = loop;
        }

        @Override
        public ArrayList<Node> getChildren() {
            ArrayList<Node> ls = new ArrayList<Node>();
            ls.add(test);
            ls.add(loop);
            return ls;
        }
        @Override
        public String toString() {
            return "While";
        }

        public Type typecheck(LocalEnvironment env) {
            Type type_test, type_loop;
            type_test = test.typecheck(env);
            if(!type_test.equals(new Type("Bool"))) {
                type = new Error_t("While statement has non-bool test type " + type_test, env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
            type = loop.typecheck(env);
            return type;
        }

        public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
            ArrayList<Instruction> ret = new ArrayList<Instruction>();
            ArrayList<Instruction> condTest = new ArrayList<Instruction>();
            ArrayList<Instruction> codeLoop = new ArrayList<Instruction>();

            condTest = test.genIR3(classId, temps);
            for(Statement s: loop.statements) {
                codeLoop.addAll(s.genIR3(classId, temps));
            }
            //connect lTrue with codeTrue and lFalse with codeFalse
            //condTest[n]; cJmp condRes lTrue; ucJmp lFalse; lTrue; codeTrue; lFalse; codeFalse;
            Label3 lLoop = new Label3();
            Label3 lTrue = new Label3();
            Label3 lFalse = new Label3();

            Arg3 condRes = Instruction.getResultFromList(condTest);
            CJmp3 cJmp = new CJmp3(condRes, new Int3(lTrue.getN()));
            Jmp3 ucJmp = new Jmp3(new Int3(lFalse.getN()));
            Jmp3 ucJmp_loop = new Jmp3(new Int3(lLoop.getN()));

            ret.add(lLoop);
            ret.addAll(condTest);
            ret.add(cJmp);
            ret.add(ucJmp);
            ret.add(lTrue);
            ret.addAll(codeLoop);
            ret.add(ucJmp_loop);
            ret.add(lFalse);

            return ret;
        }
    }
