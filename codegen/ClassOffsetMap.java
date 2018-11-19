package codegen;

import java.util.HashMap;
import java.util.Hashtable;

import ir3.ClassData;

//offset for all heap data
public class ClassOffsetMap {
    private static HashMap<String, Integer> sizes = new HashMap<>();
    private static HashMap<String, HashMap<String, Integer>> offsets = new HashMap<>();
    private static ClassData classData;

    //cache
    public ClassOffsetMap(ClassData cl) {
        classData = cl;
    }

    public static void processClass(String clName) {
        Hashtable<String, String> variables = classData.rec.get(clName);
        HashMap<String, Integer> names = new HashMap<>();
        sizes.put(clName, variables.size() * 4);
        int n = 0;
        for(String s : variables.keySet()) {
            names.put(s,n);
            n += 4;
        }
        offsets.put(clName, names);
    }

    public static Integer getObjectSize(String name) {
        if(sizes.get(name) == null)
            processClass(name);
        return sizes.get(name);
    }

    public static Integer getOffset(String cl, String var) {
        HashMap<String, Integer> names = offsets.get(cl);
        if(names == null)
            processClass(cl);
        names = offsets.get(cl);
        return names.get(var);
    }
}
