package ir3;

import java.util.ArrayList;

public abstract class Arg3 {
    public boolean isVar3() {return false;}

    public String getId() {
        return null;
    }

    public boolean isConst() {return false;}

    public abstract ArrayList<Arg3> read();

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj instanceof Arg3) {
            Arg3 arg = (Arg3) obj;
            if(arg instanceof Var3 && obj instanceof Var3) {
                return arg.equals(obj);
            }
            else if (arg instanceof Access3 && obj instanceof Access3) {
                return ((Access3) arg).fieldId.equals(((Access3) obj).fieldId)
                        && ((Access3) arg).objectId.equals(((Access3) obj).objectId);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
