package ir3;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import ast.ClassDescriptor;
import ast.Node;


public class ClassData extends Node {
    public static Hashtable<String, Hashtable<String,String>> rec;

    public ClassData() {
        rec = ClassDescriptor.extract();
    }

    public ArrayList<Node> getChildren() {
        return null;
    }

    @Override
    public String toString() {
        String ret = "";
        String curr = "";
        Hashtable<String,String> varMap;
        for(Map.Entry<String, Hashtable<String,String>> cl: rec.entrySet()) {
            curr = "class " + cl.getKey() + "\n";
            varMap = cl.getValue();
            for(Map.Entry<String,String> var: varMap.entrySet()) {
                curr = curr + "    " + var.getValue() + " " + var.getKey() + "\n";
            }
            ret += curr;
        }
        return ret;
    }

    public static String getType(String className, String variableName) {
        return rec.get(className).get(variableName);
    }
}
