package ast;

import java.util.HashMap;
import java.util.Hashtable;

public class MSig {
    public Hashtable<String, String> nameMap;

    public Hashtable<String, Tuple<HashMap<String,String>, String>> rec;

    public MSig() {
        this.nameMap = new Hashtable<String, String>();
        this.rec = new Hashtable<String, Tuple<HashMap<String,String>, String>>();
    }

    public void assignFlatName(String classId) {
        int i=0;
        for(String k: rec.keySet()) {
            this.nameMap.put(k, "%"+classId+"_"+String.valueOf(i));
            i++;
        }
    }

    public String lookup_flat_name(String name) {
        return nameMap.get(name);
    }
}
