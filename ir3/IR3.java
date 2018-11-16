package ir3;

import java.util.*;
import ast.*;

public class IR3 extends Node {
    ClassData cData;
    ArrayList<MethodData> mData;

    public IR3(Program prog) {
        cData = new ClassData();
        mData = prog.genIR3();
    }



    public ArrayList<Node> getChildren() {
        return null;
    }

    @java.lang.Override
    public java.lang.String toString() {
        String mString = "";
        for(MethodData m: mData) {
            mString = mString + m.toString() + "\n";
        }
        return "===== CData3 ===== \n" +
                cData +
                "\n===== CMtd3 ===== \n" +
                mString;
    }
}

/*
abstract class Op3 {

}
class Bop3 extends Op3 {

}
class Uop3 extends Op3 {

}
class ADD3 extends Bop3 {

}
class SUB3 extends Bop3 {

}
class MULT3 extends Bop3 {

}
class DIV3 extends Bop3 {

}
class NEG3 extends Uop3 {

}
class LE3 extends Uop3 {

}
class GE3 extends Bop3 {

}
class LT3 extends Bop3 {

}
class GT3 extends Bop3 {

}
class EQ3 extends Bop3 {

}
class AND3 extends Bop3 {

}
class OR3 extends Bop3 {

}
class NOT3 extends Uop3 {

}
class IF3 extends Op3 {

}
class ASSIGN3 extends Op3 {

}
class CALL3 extends Op3 {

}
class GOTO3 extends Op3 {

}
class LABEL3 extends Op3 {

}
class Read3 extends Op3 {

}
class Print3 extends Op3 {

}
class Ret3 extends Op3 {

}
class VRet3 extends Op3 {

}
*/

