all:
	jflex minijava.flex
	java -jar java-cup-11b.jar -interface -parser Parser minijava.cup
	javac -cp java-cup-11b-runtime.jar:. *.java ast/*.java ir3/*.java codegen/*.java analysis/BasicBlocks.java
		
	java -cp java-cup-11b-runtime.jar:. Parser 1 > 1.out
	java -cp java-cup-11b-runtime.jar:. Parser 2 > 2.out
	java -cp java-cup-11b-runtime.jar:. Parser 3 > 3.out
	java -cp java-cup-11b-runtime.jar:. Parser 4 > 4.out
	java -cp java-cup-11b-runtime.jar:. Parser 5 > 5.out
	java -cp java-cup-11b-runtime.jar:. Parser 6 > 6.out
	java -cp java-cup-11b-runtime.jar:. Parser 7 > 7.out
	java -cp java-cup-11b-runtime.jar:. Parser 8 > 8.out
	java -cp java-cup-11b-runtime.jar:. Parser 9 > 9.out
	java -cp java-cup-11b-runtime.jar:. Parser 10 > 10.out
	java -cp java-cup-11b-runtime.jar:. Parser 11 > 11.out
	java -cp java-cup-11b-runtime.jar:. Parser 12 > 12.out
compile:
	jflex minijava.flex
	java -jar java-cup-11b.jar -interface -parser Parser minijava.cup
	javac -cp java-cup-11b-runtime.jar:. *.java ast/*.java ir3/*.java codegen/*.java analysis/BasicBlocks.java

run:
	java -cp java-cup-11b-runtime.jar:. Parser 1 > 1.out
	java -cp java-cup-11b-runtime.jar:. Parser 2 > 2.out
	java -cp java-cup-11b-runtime.jar:. Parser 3 > 3.out
	java -cp java-cup-11b-runtime.jar:. Parser 4 > 4.out
	java -cp java-cup-11b-runtime.jar:. Parser 5 > 5.out
	java -cp java-cup-11b-runtime.jar:. Parser 6 > 6.out
	java -cp java-cup-11b-runtime.jar:. Parser 7 > 7.out
	java -cp java-cup-11b-runtime.jar:. Parser 8 > 8.out
	java -cp java-cup-11b-runtime.jar:. Parser 9 > 9.out
	java -cp java-cup-11b-runtime.jar:. Parser 10 > 10.out
	java -cp java-cup-11b-runtime.jar:. Parser 11 > 11.out
	java -cp java-cup-11b-runtime.jar:. Parser 12 > 12.out

clean:
	rm Lexer.java Parser.java sym.java
	rm *.class
	rm ast/*.class
	rm ir3/*.class
	rm *.out

