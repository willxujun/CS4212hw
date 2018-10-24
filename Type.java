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
}