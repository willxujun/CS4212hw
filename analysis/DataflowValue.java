package analysis;

public abstract class DataflowValue {
    public abstract DataflowValue clone();

    public void or(DataflowValue b) { }
}
