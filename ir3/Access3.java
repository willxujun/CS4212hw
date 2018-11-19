package ir3;

import java.util.ArrayList;

public class Access3 extends Arg3 {
    public String objectId;
    public String fieldId;

    public Access3(String objectId, String fieldId) {
        this.objectId = objectId;
        this.fieldId = fieldId;
    }
    public String toString() {
        return objectId + "." + fieldId;
    }

    @Override
    public ArrayList<Arg3> read() {
        ArrayList<Arg3> ls = new ArrayList<>();
        ls.add(this);
        return ls;
    }
}
