package analysis;

public abstract class TransferFunction {
    public abstract void f(DataflowValue dest, DataflowValue aux);
}