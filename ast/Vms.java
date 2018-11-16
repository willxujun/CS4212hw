package ast;

public class Vms {
    public Variables vs;
    public Methods ms;

    public Vms() {
        vs = new Variables();
        ms = new Methods();
    }

    public Vms(Variables vs) {
        this.vs = vs;
        this.ms = new Methods();
    }

    public Vms(Methods ms) {
        this.vs = new Variables();
        this.ms = ms;
    }

    public Vms(Variables vs, Methods ms) {
        this.vs = vs;
        this.ms = ms;
    }

    @Override
    public String toString() {
        return "Vms{" +
                "vs=" + vs +
                ", ms=" + ms +
                '}';
    }
}
