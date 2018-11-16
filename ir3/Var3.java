package ir3;

public class Var3 extends Arg3 {
    private String id;

    public Var3() {
    }

    public Var3(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public boolean isVar3() {return true;}

    public boolean isConst() {return false;}
}
