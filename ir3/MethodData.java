package ir3;

import java.util.ArrayList;
import java.util.HashSet;

import ast.ClassDescriptor;
import ast.Formal;
import ast.Node;
import ast.Variable;

public class MethodData extends Node {
    //grab from method signature
    public String classId;
    public String methodId;
    String retType;
    ArrayList<Formal> fmlList;
    ArrayList<Decl3> localVarList;
    ArrayList<Decl3> tempList;
    public ArrayList<Instruction> code;

    private HashSet<String> formals;
    private HashSet<String> locals;

    public void preProcessDecls() {
        formals = new HashSet<>();
        for(Formal f: fmlList) {
            formals.add(f.id);
        }
        locals = new HashSet<>();
        for(Decl3 l: localVarList) {
            locals.add(l.arg2.getId());
        }
    }

    public boolean isLocal(Var3 variable) {
        if(variable instanceof Const3)
            return false;
        else if (locals.contains(variable.getId()))
            return true;
        else
            return false;
    }

    public boolean isFormal(Var3 variable) {
        if(variable instanceof Const3)
            return false;
        else if (formals.contains(variable.getId()))
            return true;
        else
            return false;
    }

    public boolean isOnHeap(Var3 variable) {
        return !isLocal(variable) && !isFormal(variable);
    }

    public MethodData(String classId, String methodId, String retType, ArrayList<Formal> fmlList, ArrayList<Variable> localVarList, ArrayList<Instruction> code, ArrayList<Decl3> temps) {
        this.classId = classId;
        this.methodId = ClassDescriptor.lookup_flat_func_name(classId, methodId);
        this.retType = retType;
        this.fmlList = fmlList;
        this.localVarList = new ArrayList<Decl3>();
        for(Variable v: localVarList) {
            this.localVarList.add(new Decl3(v.getType(), v.getId()));
        }
        this.tempList = temps;
        this.code = code;
    }

    public ArrayList<Node> getChildren() {
        return null;
    }

    public ArrayList<Var3> getFormals() {
        ArrayList<Var3> l = new ArrayList<>();
        for(Formal f: fmlList) {
            l.add(new Var3(f.id));
        }
        return l;
    }
    public ArrayList<Var3> getLocals() {
        ArrayList<Var3> l = new ArrayList<>();
        for(Decl3 decl: localVarList) {
            l.add(decl.toVar3());
        }
        return l;
    }
    public ArrayList<Var3> getTemps() {
        ArrayList<Var3> l = new ArrayList<>();
        for(Decl3 decl: tempList) {
            l.add(decl.toVar3());
        }
        return l;
    }

    @Override
    public String toString() {
        String codeString = "";
        for(Instruction i: code) {
            codeString += "    " + i + "\n";
        }
        return retType + " " + methodId + " " + fmlList + "\n" +
                localVarList + "\n" + tempList + "\n" +
                codeString;
    }
}
