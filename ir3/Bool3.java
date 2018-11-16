package ir3;

public class Bool3 extends Const3 {
    boolean val;

    public Bool3(boolean val) {
        this.val = val;
    }

    public String toString() {
        return String.valueOf(val);
    }
}
