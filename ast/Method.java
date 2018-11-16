package ast;
import java.util.*;

import ir3.Decl3;
import ir3.Instruction;

public class Method extends Node {
    public String classId;
    public String type;
    public String id;
    public Formals formals;
    public Variables vars;
    public Statements stmts;
    public ArrayList<Decl3> temps;

    public Method(String type, String id, Formals formals, Variables vars, Statements stmts) {
        this.type = type;
        this.id = id;
        this.formals = formals;
        this.vars = vars;
        this.stmts = stmts;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(formals);
        ls.add(vars);
        ls.add(stmts);
        return ls;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return type + " " + id;
    }

    public boolean isOK(LocalEnvironment env) {
        LocalEnvironment newEnv = env.augment_mtd(this.id, vars.vars);
        Type type_stmts = stmts.typecheck(newEnv);
        if(this.type.equals(type_stmts.getId())) {
            classId = env.lookup_var("this").getId();
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Instruction> genIR3(String classId) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> curr;
        ArrayList<Decl3> tempList = new ArrayList<Decl3>();
        for(Statement s: stmts.statements) {
            curr = s.genIR3(classId, tempList);
            ret.addAll(curr);
        }
        this.temps = tempList;
        return ret;
    }

    public Tuple<Hashtable<String,String>, String> getInfo() {
        Hashtable<String,String> fmlMap = new Hashtable<String,String>();
        Tuple<Hashtable<String, String>, String> mInfo;
        ArrayList<Formal> fs = formals.formals;
        Formal fml;
        String fName, fType;
        for(int i=0; i<fs.size(); i++) {
            fml = fs.get(i);
            fName = fml.id;
            fType = fml.type;
            fmlMap.put(fName, fType);
        }
        mInfo = new Tuple<Hashtable<String, String>, String>(fmlMap, this.type);
        return mInfo;
    }
}