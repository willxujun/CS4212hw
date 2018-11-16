package ast;

import java.util.Hashtable;

public class VarSig {
    public Hashtable<String, String> rec;

    public VarSig() {
        this.rec = new Hashtable<String, String>();
    }
}
