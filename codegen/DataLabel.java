package codegen;

public class DataLabel {
    private static int n = 0;

    public static int gen() {
        n++;
        return n;
    }
}
