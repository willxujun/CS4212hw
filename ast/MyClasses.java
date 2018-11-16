package ast;

import java.util.ArrayList;

public class MyClasses {
    public static boolean hasParseError = false;
    public ArrayList<MyClass> classes;

    public MyClasses() {
        classes = new ArrayList<MyClass>();
    }

    public MyClasses append(MyClass e) {
        this.classes.add(e);
        if(e.isError()) {
            hasParseError = true;
        }
        return this;
    }

    public MyClasses append(int i, MyClass e) {
        if(e.isError()) {
            hasParseError = true;
        }
        this.classes.add(i, e);
        return this;
    }

    @Override
    public String toString() {
        return "ClassList";
    }
}
