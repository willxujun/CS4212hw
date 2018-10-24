import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

%%
%public
%class Lexer
%cup
%implements sym
%char
%line
%column

%{
    StringBuffer string = new StringBuffer();
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
    	this(in);
	    symbolFactory = sf;
    }
    ComplexSymbolFactory symbolFactory;

  private Symbol symbol(String name, int sym) {
       return symbolFactory.newSymbol(name, sym, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+yylength(),yychar+yylength()));
  }

  private Symbol symbol(String name, int sym, Object val) {
      Location left = new Location(yyline+1,yycolumn+1,yychar);
      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
  }

  private Symbol symbol(String name, int sym, Object val,int buflength) {
      Location left = new Location(yyline+1,yycolumn+yylength()-buflength,yychar+yylength()-buflength);
      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
  }

  private void error(String message) {
    System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
  }
%}

%eofval{
     return symbolFactory.newSymbol("EOF", EOF, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+1,yychar+1));
%eofval}

DEC = [0-9][0-9][0-9]
HEX = [0-9a-fA-F][0-9a-fA-F]
NUM = 0 | [1-9][0-9]*
NEWLINE = \r|\n|\r\n;
WHITESPACE = {NEWLINE} | [ \t\f]
OBJECTID = [a-z][_a-zA-Z0-9]*
TYPEID = [A-Z][_a-zA-Z0-9]*

/*Keywords*/
K_CLASS     = "class"
K_IF        = "if"
K_ELSE      = "else"
K_WHILE     = "while"
K_READLN    = "readln"
K_PRINTLN   = "println"
K_RETURN    = "return"
K_TRUE      = "true"
K_FALSE     = "false"
K_THIS      = "this"
K_NEW       = "new"
K_NULL      = [nN][uU][lL][lL]

%state STRING
%state COMMENT
%state COMMENT_SINGLE

%%

<YYINITIAL>{
    /* keywords */
    { K_CLASS   }     { return symbol("class",CLASS); }
    { K_IF      }     { return symbol("if",IF); }
    { K_ELSE    }     { return symbol("else",ELSE); }
    { K_WHILE   }     { return symbol("while",WHILE); }
    { K_READLN  }     { return symbol("readln",READLN); }
    { K_PRINTLN }     { return symbol("println",PRINTLN); }
    { K_RETURN  }     { return symbol("return",RETURN); }
    { K_TRUE    }     { return symbol("true",BOOLCONST,Boolean.valueOf(true)); }
    { K_FALSE   }     { return symbol("false",BOOLCONST,Boolean.valueOf(false)); }
    { K_THIS    }     { return symbol("this",THIS); }
    { K_NEW     }     { return symbol("new",NEW); }
    { K_NULL    }     { return symbol("null",NULL); }
 
    /* literals */
    {NUM}      { return symbol("Intconst",INTCONST, new Integer(Integer.parseInt(yytext()))); }
    {TYPEID}       { return symbol("TypeId", TYPEID, yytext()); }
    {OBJECTID}      { return symbol("ObjectId", OBJECTID, yytext()); }

  
    /* operators */
    ";"               { return symbol(";",SEMICOLON); }
    "{"               { return symbol("{",LBRACES); }  
    "}"               { return symbol("}",RBRACES); }  
    "("               { return symbol("(",LPAREN); }
    ")"               { return symbol(")",RPAREN); }
    ","               { return symbol(",",COMMA); }   
    "."               { return symbol(".",DOT); } 
    "||"              { return symbol("||",OR); } 
    "&&"              { return symbol("&&",AND); }   
    "!"               { return symbol("!",NOT); }  
    "+"               { return symbol("+",PLUS); }
    "-"               { return symbol("-",MINUS); }
    "*"               { return symbol("*",TIMES); }
    "/"               { return symbol("/",DIV); }
    "<"               { return symbol("<",LT); }  
    ">"               { return symbol(">",GT); }
    "<="              { return symbol("<=",LE); }
    ">="              { return symbol(">=",GE); }
    "!="              { return symbol("!=",NE); }
    "=="              { return symbol("==",EQ); }
    "="               { return symbol("=",ASSIGN); }
   
    /* string beginning */
    \"                { string.setLength(0);
                        yybegin(STRING); }

    /*comment beginning*/
    "/*"              { yybegin(COMMENT); }
    "//"              { yybegin(COMMENT_SINGLE); }

    {WHITESPACE}     {/* ignore */ }

}

<COMMENT_SINGLE> {
    \n                {yybegin(YYINITIAL);}
    [^]               {}
}

<STRING> {
    \"                             { yybegin(YYINITIAL);
                                     return symbol("StrConst", STRCONST, string.toString()); }
    \\{DEC}                        { 
                                        int val = Integer.parseInt(yytext().substring(1, 4), 10);
                                        if(val < 0 || val > 127)
                                            error("not an ascii character <"+ yytext()+">");
                                        string.append((char)val); 
                                   }

    \\x{HEX}                       {
                                        int val = Integer.parseInt(yytext().substring(2, 4), 16);
                                        if(val < 0 || val > 127)
                                            error("not an ascii character <"+ yytext()+">");
                                        string.append((char)val); 
                                    }
    [^\n\r\"\\]+                   { string.append( yytext() ); }
    \\t                            { string.append('\t'); }
    \\n                            { string.append('\n'); }

    \\r                            { string.append('\r'); }
    \\\"                           { string.append('\"'); }
    \\                             { string.append('\\'); }

    <<EOF>>                        { yybegin(YYINITIAL); 
                                     error("unterminated string <"+ yytext()+">"); }
}

<COMMENT> {
    "*/"                            {yybegin(YYINITIAL);}
    <<EOF>>                        { 
                                        yybegin(YYINITIAL);
                                        error("unterminated comment <"+ yytext()+">"); }
    [^]                             {}
}

/* error fallback */
[^]              {  /* throw new Error("Illegal character <"+ yytext()+">");*/
		                error("Illegal character <"+ yytext()+">");
                 }
