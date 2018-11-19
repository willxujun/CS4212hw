package codegen;

//map from ir3 names to pseudo registers
public class Temp {
    private static int n = 0;
    public int num;

    public Temp() {
        num = n;
        n++;
    }
}