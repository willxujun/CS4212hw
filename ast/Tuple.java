package ast;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;
    
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "{" +
                "typeId:" + x +
                ", fieldName:" + y +
                '}';
    }
}

