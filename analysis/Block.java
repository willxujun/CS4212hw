package analysis;

import java.util.ArrayList;

import Graph.Node;
import asm.Insn;
import ir3.Instruction;

public class Block extends Node {
    public Integer label;
    public ArrayList<Integer> gen;
    public ArrayList<Integer> kill;

    public ArrayList<Instruction> ir3Code;
    public ArrayList<Insn> armCode;

    private int jumpTo;
    //private ArrayList<BitVec> programPoints;

    public Block() {}

    public Block(int label) {
        this.label = label;
        ir3Code = new ArrayList<>();
        //programPoints = new ArrayList<>();
        jumpTo = -1;
    }

    /*
    public void setOut1(int out1) {
        this.out1 = out1;
    }

    public void setOut2(int out2) {
        this.out2 = out2;
    }
    */

    public void setJumpTo(int jumpTo) {
        this.jumpTo = jumpTo;
    }

    public int getJumpTo() {
        return jumpTo;
    }

    public void add(Instruction i) {
        ir3Code.add(i);
    }

    public void addPred(Block b) {
        pred.add(b);
    }

    public void addSucc(Block b) {
        succ.add(b);
    }

    public Integer getLabel() {
        return label;
    }

    public void show() {
        System.out.println("$$$$$ IR3 block begins $$$$$");
        for(Instruction i : ir3Code)
            System.out.println(i);
    }

    //ir3 to arm transformation preserving block structure
    public void toArm() {
        armCode = new ArrayList<>();
        ArrayList<Insn> tmp;
        for(Instruction i: ir3Code) {
            tmp = Insn.fromIR3(i);
            armCode.addAll(tmp);
        }
        ir3Code = null;
    }

}
