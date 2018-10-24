import java.util.*;

class Expressions extends Node{
    public ArrayList<Expression> exps;

    public Expressions() {
        exps = new ArrayList<Expression>();
    }

    public Expressions(Expression e) {
        exps = new ArrayList<Expression>();
        exps.add(e);
    }

    public Expressions append(Expression e) {
        this.exps.add(e);
        return this;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        ArrayList<Node> ls = new ArrayList<Node>(exps);
        return ls;
    }
    @java.lang.Override
    public java.lang.String toString() {
        return "ExpressionList";
    }

    public int check_exps(HashMap<String,String> formalMap, LocalEnvironment env) {
        if(exps.size() != formalMap.size()) {
            return 1;
        }
        int i = 0;
        Expression curr;
        Type exp_type;
        for(Map.Entry<String,String> entry: formalMap.entrySet()) {
            curr = exps.get(i);
            exp_type = curr.typecheck(env);
            if(exp_type.equals(new Error_t())) {
                return 2;
            }
            if(!exp_type.equals(new Type(entry.getValue()))) {
                return 3;
            }
            i++;
        }
        return 0;
    }
}