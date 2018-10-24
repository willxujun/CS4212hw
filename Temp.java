public class Temp extends Var3 {
    private static int qty = 0;
    private int id;
    private String type;

    public Temp(String type) {
        this.type = type;
        this.id = qty;
        qty++;
    }

    public Temp(Type type) {
        this.type = type.getId();
        this.id = qty;
        qty++;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return "t" + String.valueOf(this.id);
    }
}