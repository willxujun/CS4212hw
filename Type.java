import java.util.*;

public class Type {
    private String id;

    public Type() {
        id = "";
    }
    public Type(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean equals(Object object) {
        if(object instanceof Type) {
            Type type = (Type)object;
            if(type.getId().equals(this.id)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return id;
    }

    public void printMsg() {
        System.out.println("");
    }

    public boolean isError() {
        return false;
    }

    public boolean isTuple() {return false;}

    public boolean isNull() {return false;}

    public int match(Type t) {
        return -1;
    }

    public boolean isFunction() {return false;}
}

class Tuple_t extends Type {
    private final ArrayList<Type> ids;

    public Tuple_t(ArrayList<Type> ids) {
        super();
        this.ids = ids;
    }

    public Tuple_t(Type first, Type snd) {
        super();
        ArrayList<Type> newIds = new ArrayList<Type>();
        newIds.add(first);
        newIds.add(snd);
        ids = newIds;
    }

    public ArrayList<Type> getIds() {
        return ids;
    }

    public boolean isTuple() {return true;}

    public Type fst() {
        return ids.get(0);
    }
    public Type snd() {
        return ids.get(1);
    }

}

class Function_t extends Type {
    private ArrayList<String> formal_types;

    public Function_t(String ret, String... formalTypes) {
        super(ret);
        formal_types = new ArrayList<String>(Arrays.asList(formalTypes));
    }
    public Function_t(String id, ArrayList<String> formal_types) {
        super(id);
        this.formal_types = formal_types;
    }
    public int match(Tuple_t t) {
        ArrayList<Type> type_list = t.getIds();
        if(type_list.size() != formal_types.size())
            return 1;
        String exp_type_id;
        for(int i=0; i<formal_types.size(); i++) {
            exp_type_id = type_list.get(i).getId();
            if(!exp_type_id.equals(formal_types.get(i)))
                return 2;
        }
        return 0;
    }
    public String getReturnType() {
        return super.getId();
    }
    public boolean isFunction() {return true;}
}

class Error_t extends Type {
    private String msg;

    public Error_t() {
        super();
        msg = "";
    }
    public Error_t(String msg, String currMethod, String currClass) {
        super();
        this.msg = "Error: " + msg + "\nIn method " + currMethod + "\nIn class " + currClass + "\n";
    }
    public Error_t(String msg) {
        super();
        this.msg = msg;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Type) {
            return true;
        }
        return false;
    }

    public void printMsg() {
        System.out.println(msg);
    }

    public boolean isError() {
        return true;
    }

    public String getMsg() {return msg;}

    @Override
    public String toString() {
        return "Error_t";
    }
}

class Null_t extends Type {
    @Override
    public boolean equals(Object object) {
        if(object instanceof Type) {
            return true;
        }
        return false;
    }

    public boolean isError() {
        return false;
    }

    public boolean isNull() { return true; }
}