package ir3;

public class Obj3 extends Instruction {
    Var3 name;
    Var3 fieldName;

    public Obj3() {}
    public Obj3(String name) {
        this.name = new Var3(name);
    }
    public Obj3(Const3 constant) { this.name = constant; }

    public boolean isObj() {
        return true;
    }

    public Var3 getResult() {
        return name;
    }

}
