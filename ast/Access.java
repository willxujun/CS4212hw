package ast;

import java.util.ArrayList;

import ir3.Access3;
import ir3.Assign3;
import ir3.Decl3;
import ir3.Instruction;
import ir3.Obj3;
import ir3.Var3;

public class Access extends Atom {
    public Atom obj;
    public String fieldName;
    public Type type;

    public Type callerClass;
    public String callerId;
    public String funcId;

    public Access(Atom obj, String fieldName) {
        this.obj = obj;
        this.fieldName = fieldName;
    }

    @Override
    public String getCallerId() {
        return callerId;
    }

    @Override
    public Type getCallerClass() {
        return callerClass;
    }

    @Override
    public String getFuncId() {
        return funcId;
    }

    @Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>();
        ls.add(obj);
        return ls;
    }
    @Override
    public String toString() {
        return "Access " + fieldName;
    }

    @Override
    public boolean isAccess() {
        return true;
    }

    public Type typecheck(LocalEnvironment env) {
        //left type cannot be Function_t
        Type obj_type = obj.typecheck(env);
        if(obj_type.isError()) {
            type = new Error_t("Accessing object does not type check when accessing field " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        /*
        if(obj_type.isFunction()) {
            type = new Error_t("Access LHS cannot be function type");
            type.printMsg();
            return type;
        }
        if(obj_type.isTuple()) {
            obj_type = obj_type.fst();
        }
        */
        //overall type cannot be function
        Type var_type = ClassDescriptor.lookup_var(obj_type.getId(), fieldName);
        if(var_type.isError()) {
            type = new Error_t("Access cannot find field name " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        this.type = var_type;
        return type;
    }

    public Type typecheck_func(LocalEnvironment env) {
        Type obj_type = obj.typecheck(env);
        if(obj_type.isError()) {
            type = new Error_t("Dispatch object does not type check for call to " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        Type func_type = ClassDescriptor.lookup_func(obj_type.getId(), fieldName);
        if(func_type.isError()) {
            type = new Error_t("Call to unknown function " + fieldName, env.currMethod, env.currClass);
            type.printMsg();
            return type;
        }
        this.type = func_type;
        //fill up function info
        callerClass = obj_type;
        funcId = fieldName;
        if(obj.isId()) {
            callerId = ((Id)obj).objectId;
        } else if(obj.isAccess()) {
            callerId = ((Access)obj).fieldName;
        } else {
            //nameless obj
        }
        return type;
    }

    public ArrayList<Instruction> genIR3(String classId, ArrayList<Decl3> temps) {
        ArrayList<Instruction> ret = new ArrayList<Instruction>();
        ArrayList<Instruction> code = obj.genIR3(classId, temps);
        Var3 res = Instruction.getResultFromList(code);
        ret.addAll(code);

        Temp t;
        if(!type.isFunction()) {
            t = new Temp(type);
            temps.add(new Decl3(t));
            ret.add(new Assign3(t,
                    new Access3(res.getId(), fieldName)));
            return ret;
        } else {
            callerId = res.getId();
            ret.add(new Obj3(callerId));
            return ret;
        }
    }
}
