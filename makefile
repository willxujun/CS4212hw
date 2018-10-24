all:
	jflex minijava.flex
	java -jar java-cup-11b.jar -interface -parser Parser minijava.cup
	javac -cp java-cup-11b-runtime.jar:. *.java
	java -cp java-cup-11b-runtime.jar:. Parser test1.pl > 1.out
	java -cp java-cup-11b-runtime.jar:. Parser test2.pl > 2.out
	java -cp java-cup-11b-runtime.jar:. Parser test3.pl > 3.out
	java -cp java-cup-11b-runtime.jar:. Parser test4.pl > 4.out
	java -cp java-cup-11b-runtime.jar:. Parser test5.pl > 5.out
	java -cp java-cup-11b-runtime.jar:. Parser test6.pl > 6.out
	java -cp java-cup-11b-runtime.jar:. Parser test7.pl > 7.out
	
	
	java -cp java-cup-11b-runtime.jar:. Parser 1 > 1.1.out
	java -cp java-cup-11b-runtime.jar:. Parser 2 > 1.2.out
	java -cp java-cup-11b-runtime.jar:. Parser 3 > 1.3.out
	java -cp java-cup-11b-runtime.jar:. Parser 4 > 1.4.out
	java -cp java-cup-11b-runtime.jar:. Parser 5 > 1.5.out
	java -cp java-cup-11b-runtime.jar:. Parser 6 > 1.6.out
	java -cp java-cup-11b-runtime.jar:. Parser 7 > 1.7.out
	java -cp java-cup-11b-runtime.jar:. Parser 8 > 1.8.out
	java -cp java-cup-11b-runtime.jar:. Parser 9 > 1.9.out
	java -cp java-cup-11b-runtime.jar:. Parser 10 > 1.10.out
	java -cp java-cup-11b-runtime.jar:. Parser 11 > 1.11.out
	java -cp java-cup-11b-runtime.jar:. Parser 12 > 1.12.out
	java -cp java-cup-11b-runtime.jar:. Parser 13 > 1.13.out
	java -cp java-cup-11b-runtime.jar:. Parser 14 > 1.14.out
	java -cp java-cup-11b-runtime.jar:. Parser 15 > 1.15.out
	java -cp java-cup-11b-runtime.jar:. Parser 16 > 1.16.out
	java -cp java-cup-11b-runtime.jar:. Parser 17 > 1.17.out
	java -cp java-cup-11b-runtime.jar:. Parser 18 > 1.18.out
	java -cp java-cup-11b-runtime.jar:. Parser 19 > 1.19.out
	java -cp java-cup-11b-runtime.jar:. Parser 20 > 1.20.out

compile:
	jflex minijava.flex
	java -jar java-cup-11b.jar -interface -parser Parser minijava.cup
	javac -cp java-cup-11b-runtime.jar:. *.java

run:
	java -cp java-cup-11b-runtime.jar:. Parser test1.pl > 1.out
	java -cp java-cup-11b-runtime.jar:. Parser test2.pl > 2.out
	java -cp java-cup-11b-runtime.jar:. Parser test3.pl > 3.out
	java -cp java-cup-11b-runtime.jar:. Parser test4.pl > 4.out
	java -cp java-cup-11b-runtime.jar:. Parser test5.pl > 5.out
	java -cp java-cup-11b-runtime.jar:. Parser test6.pl > 6.out
	java -cp java-cup-11b-runtime.jar:. Parser test7.pl > 7.out
	java -cp java-cup-11b-runtime.jar:. Parser 1 > 1.1.out
	java -cp java-cup-11b-runtime.jar:. Parser 2 > 1.2.out
	java -cp java-cup-11b-runtime.jar:. Parser 3 > 1.3.out
	java -cp java-cup-11b-runtime.jar:. Parser 4 > 1.4.out
	java -cp java-cup-11b-runtime.jar:. Parser 5 > 1.5.out
	java -cp java-cup-11b-runtime.jar:. Parser 6 > 1.6.out
	java -cp java-cup-11b-runtime.jar:. Parser 7 > 1.7.out
	java -cp java-cup-11b-runtime.jar:. Parser 8 > 1.8.out
	java -cp java-cup-11b-runtime.jar:. Parser 9 > 1.9.out
	java -cp java-cup-11b-runtime.jar:. Parser 10 > 1.10.out
	java -cp java-cup-11b-runtime.jar:. Parser 11 > 1.11.out
	java -cp java-cup-11b-runtime.jar:. Parser 12 > 1.12.out
	java -cp java-cup-11b-runtime.jar:. Parser 13 > 1.13.out
	java -cp java-cup-11b-runtime.jar:. Parser 14 > 1.14.out
	java -cp java-cup-11b-runtime.jar:. Parser 15 > 1.15.out
	java -cp java-cup-11b-runtime.jar:. Parser 16 > 1.16.out
	java -cp java-cup-11b-runtime.jar:. Parser 17 > 1.17.out
	java -cp java-cup-11b-runtime.jar:. Parser 18 > 1.18.out
	java -cp java-cup-11b-runtime.jar:. Parser 19 > 1.19.out
	java -cp java-cup-11b-runtime.jar:. Parser 20 > 1.20.out


clean:
	rm Lexer.java Parser.java sym.java
	rm *.class
	rm *.out

