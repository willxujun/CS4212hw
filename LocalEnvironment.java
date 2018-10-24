import java.util.*;

public class LocalEnvironment {
    public final Hashtable<String,String> vBindings;
    public final Hashtable<String, Tuple<HashMap<String,String>, String>> mSignatures;
    public String currClass;
    public String currMethod;
    
    public LocalEnvironment() {
        vBindings = new Hashtable<String,String>();
        mSignatures = new Hashtable<String, Tuple<HashMap<String,String>, String>>();
        currClass = "";
        currMethod = "";
    }

    public Type lookup_var(String varName) {
        String ret = vBindings.get(varName);
        if(ret == null)
            return null;
        return new Type(ret);
    }

    public Tuple<HashMap<String,String>, String> lookup_mtd(String mName) {
        return mSignatures.get(mName);
    }

    public Type lookup_ret_type() {
        String ret = vBindings.get("Ret");
        if(ret == null)
            return null;
        return new Type(ret);
    }

    //augment class information, including variable declarations, method signatures, and a binding to this
    public LocalEnvironment augment(String className) {
        LocalEnvironment newEnv = new LocalEnvironment();
        newEnv.vBindings.putAll(this.vBindings);
        newEnv.mSignatures.putAll(this.mSignatures);

        Tuple<VarSig, MSig> classInfo = ClassDescriptor.lookup(className);
        newEnv.vBindings.putAll(classInfo.x.rec);
        newEnv.mSignatures.putAll(classInfo.y.rec);

        newEnv.vBindings.put("this", className);
        newEnv.currClass = className;
        return newEnv;
    }

    //augment method signature info, returning a deep copy
    public LocalEnvironment augment_mtd(String methodName, ArrayList<Variable> vs) {
        LocalEnvironment newEnv = new LocalEnvironment();
        //copy old information
        newEnv.vBindings.putAll(this.vBindings);
        newEnv.mSignatures.putAll(this.mSignatures);

        Tuple<HashMap<String, String>, String> methodInfo = this.mSignatures.get(methodName);
        newEnv.vBindings.putAll(methodInfo.x);
        //because variable name starts with lower case, Ret will have unique entry in vBindings.
        newEnv.vBindings.put("Ret", methodInfo.y);

        Variable v;
        String vType, vId;
        for(int i=0; i<vs.size(); i++) {
            v = vs.get(i);
            vType = v.type;
            vId = v.id;
            newEnv.vBindings.put(vId, vType);
        }
        newEnv.currClass = this.currClass;
        newEnv.currMethod = methodName;
        return newEnv;
    }
}
