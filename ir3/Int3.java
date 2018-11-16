package ir3;

public class Int3 extends Const3 {
    int val;

    public Int3(int val) {
        this.val = val;
    }

    public String toString() {
        return String.valueOf(val);
    }
}
