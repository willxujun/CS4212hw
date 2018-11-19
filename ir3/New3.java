package ir3;

import java.util.ArrayList;

public class New3 extends Arg3 {
    String classId;

    public New3(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "new " + classId + "()";
    }

    @Override
    public ArrayList<Var3> read() {
        return new ArrayList<>();
    }
}
