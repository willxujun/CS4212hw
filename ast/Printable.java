package ast;

import java.util.List;

public class Printable {
    public static String pad = "  ";
    public static String listToString(List<?> ls) {
        String result = "";
        for(int i=0; i<ls.size(); i++) {
            result += pad + ls.get(i).toString() + '\n';
        }
        return result;
    }

    public static String parens(String s) {
        return "(" + s + ")";
    }
}
