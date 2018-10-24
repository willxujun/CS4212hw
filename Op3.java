public enum Op3 {
    ADD, SUB, MULT, DIV,
    NEG, NOT,
    LE, GE, LT, GT, EQ, NE,
    AND, OR,
    IF, ASSIGN,
    PARAM, CALL, GOTO,
    LABEL, DECL,
    READ, PRINT,
    RET, VRET;

    @Override
    public String toString() {
        switch(this) {
        case ADD: return "+";
        case SUB: return "-";
        case MULT: return "*";
        case DIV: return "/";
        case NEG: return "-";
        case NOT: return "!";
        case LE: return "<=";
        case GE: return ">=";
        case LT: return "<";
        case GT: return ">";
        case EQ: return "==";
        case NE: return "!=";
        case AND: return "&&";
        case OR: return "||";
        case IF: return "if";
        case ASSIGN: return "=";
        case PARAM: return "PARAM";
        case CALL: return "CALL";
        case GOTO: return "goto";
        case LABEL: return "L";
        case DECL: return "DECL";
        case READ: return "readln";
        case PRINT: return "println";
        case RET: return "return";
        case VRET: return "return";
        default: throw new IllegalArgumentException();
        }
    }
}