package ast;

import java.util.*;

public class Statements extends Node{
    public ArrayList<Statement> statements;

    public Statements() {
        statements = new ArrayList<Statement>();
    }

    public Statements(Statement s) {
        statements = new ArrayList<Statement>();
        statements.add(s);
    }

    public Statements append(Statement s) {
        this.statements.add(s);
        return this;
    }

    @java.lang.Override
    public ArrayList<Node> getChildren() {
        return new ArrayList<Node>(statements);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "StatementList";
    }

    public Type typecheck(LocalEnvironment env) {
        Statement stmt;
        if(statements.isEmpty()) {
            return new Error_t("empty statement sequence", env.currMethod, env.currClass);
        }
        Type curr = new Null_t();
        Type ret = new Null_t();
        for(int i=0; i<statements.size(); i++) {
            stmt = statements.get(i);
            curr = stmt.typecheck(env);
            if(curr.isError()) {
                ret = curr;
            }
        }
        if(!ret.isError()) {
            ret = curr;
        }
        return ret;
    }
}