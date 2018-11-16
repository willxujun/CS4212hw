package ir3;

import java.util.ArrayList;

import ast.ClassDescriptor;
import ast.Formal;
import ast.Node;
import ast.Variable;

public class MethodData extends Node {
    //grab from method signature
    String classId;
    String methodId;
    String retType;
    ArrayList<Formal> fmlList;
    ArrayList<Decl3> localVarList;
    ArrayList<Instruction> code;

    public MethodData(String classId, String methodId, String retType, ArrayList<Formal> fmlList, ArrayList<Variable> localVarList, ArrayList<Instruction> code, ArrayList<Decl3> temps) {
        this.classId = classId;
        this.methodId = ClassDescriptor.lookup_flat_func_name(classId, methodId);
        this.retType = retType;
        this.fmlList = fmlList;
        this.localVarList = new ArrayList<Decl3>();
        for(Variable v: localVarList) {
            this.localVarList.add(new Decl3(v.getType(), v.getId()));
        }
        this.localVarList.addAll(temps);
        this.code = code;
    }

    public ArrayList<Node> getChildren() {
        return null;
    }

    @Override
    public String toString() {
        String codeString = "";
        for(Instruction i: code) {
            codeString += "    " + i + "\n";
        }
        return retType + " " + methodId + " " + fmlList + "\n" +
                localVarList + "\n" +
                codeString;
    }
}
