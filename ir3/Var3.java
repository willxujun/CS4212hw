package ir3;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Var3 extends Arg3 {
    private String id;

    public Var3() {
    }

    public Var3(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean isVar3() {return true;}

    public boolean isConst() {return false;}

    @Override
    public ArrayList<Arg3> read() {
        ArrayList<Arg3> ls = new ArrayList<>();
        ls.add(this);
        return ls;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if (!(o instanceof Var3)) {
            return false;
        }
        Var3 variable = (Var3)o;
        if(variable instanceof Int3 && this instanceof Int3)
            return ((Int3) variable).getVal() == ((Int3) this).getVal();
        else if (variable instanceof Str3 && this instanceof Str3)
            return ((Str3) variable).getVal().equals(((Str3)this).getVal());
        else if (variable instanceof Bool3 && this instanceof Bool3)
            return ((Bool3) variable).getVal() == ((Bool3)this).getVal();
        else if (variable.id != null && this.id != null)
            return variable.id.equals(this.id);
        else
            return false;
    }
}
