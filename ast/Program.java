package ast;

import java.util.*;
import ir3.*;

public class Program extends Node {
    public static boolean hasParseError = false;
    public MyClasses classes;

    public Program(MyClasses classes) {
        hasParseError = MyClasses.hasParseError;
        this.classes = classes;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(classes.classes);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Program";
    }

    public boolean isOK() {
        LocalEnvironment env = new LocalEnvironment();
        boolean res = true;
        ArrayList<MyClass> ls = classes.classes;
        MyClass cl;
        for (int i=0; i<ls.size(); i++) {
            cl = ls.get(i);
            res = res && cl.isOK(env);
        }
        return res;
    }

    public ArrayList<MethodData> genIR3() {
        ArrayList<MethodData> ret = new ArrayList<MethodData>();
        MethodData curr;
        ArrayList<Instruction> code;

        ClassDescriptor.flattenFuncNames();
        for(MyClass cl: classes.classes) {
            for(Method mtd: cl.ms.ms) {
                code = mtd.genIR3(cl.id);
                mtd.formals.formals.add(0, new Formal(cl.id, "this"));
                curr = new MethodData(cl.id, mtd.id, mtd.type, mtd.formals.formals, mtd.vars.vars, code, mtd.temps);
                ret.add(curr);
            }
        }
        return ret;
    }
}

