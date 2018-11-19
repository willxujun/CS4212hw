package ir3;

public class Str3 extends Const3 {
    String val;

    public Str3(String val) {
        this.val = val;
    }

    public String toString() {
        return "\"" + val + "\"";
    }

    public String getVal() {
        return val;
    }
}
