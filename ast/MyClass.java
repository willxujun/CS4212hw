package ast;

import java.util.ArrayList;

public class MyClass extends Node {
    public String id;
    public Variables vs;
    public Methods ms;

    public MyClass() {
    }

    public MyClass(String id, Variables vs, Methods ms) {
        this.id = id;
        this.vs = vs;
        this.ms = ms;

        //put inside class descriptor and do distinct name check
    }

    public boolean isError() {
        return false;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(vs);
        ls.add(ms);
        return ls;
    }

    @Override
    public String toString() {
        return "Class " + id;
    }

    public boolean isOK(LocalEnvironment env) {
        LocalEnvironment newEnv = env.augment(this.id);
        boolean res = true;
        ArrayList<Method> mList = ms.ms;
        Method m;
        for (int i=0; i<mList.size(); i++) {
            m = mList.get(i);
            res = res && m.isOK(newEnv);
        }
        return res;
    }
}
