/* Mainly test multiple class (defined later but referenced first), Variable shadowing in Dummy class,
chained field access expressions,
e.g. this.getCompute().square(-3);
Test combination of "if .. else .." "return" and "while" */
class Main {
    Void main(Int i, Bool a, String b,Void d){
        //variables
        Int t1;
        Bool t2;
        String t3;
        Void t4;
        Compute help;
        
        //statements
        if(t2>t1){
            println("Square of \x75\x54\x6f\xff \999 d larger than sum of squares"); }
        else{
            println("Square of d larger than sum of squares");
        }
        
        while(true){
            // t1 = 1*2;
            t1 = t2 ;
        }
        help = new Compute();
        help.chachedValue = t1 * 3;
        atom(exp1).id = exp2;
        t1 = help.addSquares(a,b) + help.square(i);
        t2 = help.square(d);
        
        //not sure if below is the right construct
        staticmtd(arg1)(123);
        
        dynamic.dispatch(b);
        
        readln(id);
        println(a.fieldname1);
        return 1;
        return;
    }
}
class Dummy {
    Compute c; Int i; Dummy j;
    Int dummy() {
        Bool i;
        Bool j;
        if (i || j) {
            return 1;
        }
        else {
            while(i) {
                i = !j;
            }
            c = this.getCompute();
        }
        return this.getCompute().square(-3);
        return i ;
    }
    Compute getCompute() {
        // c = new Compute();
        return c;
    }
}
