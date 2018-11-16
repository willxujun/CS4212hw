package ast;

import java.util.ArrayList;

import ir3.Decl3;
import ir3.Instruction;
import ir3.Read3;
import ir3.Var3;

public class ReadStmt extends Statement {
        public String intoId;
        public Type type;

        public ReadStmt(String intoId) {
            this.intoId = intoId;
        }

        @Override
        public ArrayList<Node> getChildren() {
            return new ArrayList<Node>();
        }
        @Override
        public String toString() {
            return "Read " + intoId;
        }

        public Type typecheck(LocalEnvironment env) {
            Type type_id = env.lookup_var(intoId);
            if (type_id.isError()) {
                type = new Error_t("Unknown variable binding " + intoId, env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
            if(type_id.equals(new Type("Int")) || type_id.equals(new Type("Bool")) || type_id.equals(new Type("String"))) {
                type = new Type("Void");
                return type;
            }
            else {
                type = new Error_t("Reading into variable of the wrong type. Correct type is: Int | String | Bool", env.currMethod, env.currClass);
                type.printMsg();
                return type;
            }
        }

        public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
            ArrayList<Instruction> ret = new ArrayList<Instruction>();
            ret.add(new Read3(new Var3(intoId)));
            return ret;
        }
    }
