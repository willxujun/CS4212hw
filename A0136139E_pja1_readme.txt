This is a JLite lexer/parser built using JFLEX and CUP. 
It lexes and parses a JLite program and builds an AST out of a successful parse.
It supports basic error recovery from ill-formed but well-terminated class definitions (see test case 5).

You can run it on a Unix system by the following commands.

OPTIONS:
make all: compile and run on the input files of test1.pl thru test7.pl. Output AST is pretty printed and written into *.out respectively.
make compile: compile the Lexer and Parser
make run: run on the input files of test1.pl thru test7.pl. Output AST is pretty printed and written into *.out respectively.
make clean: clean the project

Description of directory:
minijava.flex	jflex source file
minijava.cup	CUP source file
Program.java	a java implementation of the parse tree.
TypeTable.java	a table that keeps a record of classes' names, each with its variables' names and types
test1.pl	test case 1, which is sample program 1 on handout
test2.pl	test case 2, which is sample program 2 on handout
test3.pl	test case 3, which is test case 2 enhanced with more constructs
test4.pl	test case 4, which tests class structure. Main class has more than 1 method for which the parser gives a warning.
test5.pl	test case 5, which contains parse error that could be recovered
test6.pl	test case 6, which contains syntactically correct but Int-returning main method. The parser gives a warning for this.
test7.pl	test case 6, which contains syntactically correct but wrongly named main method. The parser gives a warning for this.

Important information:
No eval() or scope-managing symbol table is implemented yet.
The parse tree represents expressions as-is.
This means "new Box()" as specified in handout will not be evaluated to create any new object.
However, a TypeTable that keeps a record of classes' names, their variables' names and types is implemented. This later will give eval() sufficient info to instantiate objects.
This information is also written to *.out.