package ir3;

public class Int3 extends Const3 {
    public int val;

    public Int3(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public String toString() {
        return String.valueOf(val);
    }
}
