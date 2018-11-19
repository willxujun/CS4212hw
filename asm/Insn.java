package asm;

import static asm.Op.*;

import java.util.ArrayList;

import ir3.Instruction;

import codegen.ClassOffsetMap;

public class Insn {
    Op op;
    Cond cond;
    boolean S;
    boolean I;
    Integer Rd;
    Integer Rn;

    //src2
    Integer Rm;
    Integer imm;

    //ldr/str
    boolean sudo;
    Integer offset;
    Integer label;
    boolean preIndexed;

    //if ldr/str multiple
    ArrayList<Integer> RegList;

    //label
    Integer num;

    public Insn() {}

    //emit compulsory ldr when name is on heap
    //emit compulsory str when mutating heap data
    public static ArrayList<Insn> fromIR3(Instruction i) {

    }

}

class DP extends Insn {
    public DP(Op op, Cond cond, boolean S, boolean I, int Rd, int Rn, int Rm, int imm) {
        super.op = op;
        super.cond = cond;
        super.S = S;
        super.I = I;
        if(I)
            super.imm = imm;
        else
            super.Rm = Rm;

        switch(op) {
        case add:
        case sub:
        case rsb:
        case and:
        case orr:
        case eor:
            super.Rd = Rd;
            super.Rn = Rn;
            break;
        case cmp:
        case cmn:
        case tst:
        case teq:
            super.Rn = Rn;
            break;
        case mov:
        case mvn:
            super.Rd = Rd;
        default:
        }
    }
}

class Mem extends Insn {
    public Mem(Op op, Cond cond, int Rn, int Rd, boolean sudo,
               int label,
               int offset, boolean preIndexed) {
        super.op = op;
        super.cond = cond;
        super.Rd = Rd;
        super.sudo = sudo;

        if(sudo) {
            super.label = label;
        } else if(offset != 0) {
            super.offset = offset;
            super.Rn = Rn;
            super.preIndexed = preIndexed;
        } else {
            super.offset = 0;
            super.Rn = Rn;
        }
    }

    public Mem(Op op, ArrayList<Integer> RegList) {
        super.op = op;
        super.RegList = RegList;
    }
}

class Branch extends Insn {
    public Branch(Op op, Cond cond, int label, int Rm) {
        super.op = op;
        super.cond = cond;
        switch(op) {
        case bl:
        case b:
            super.label = label;
            break;
        case bx:
            super.Rm = Rm;
        default:
        }
    }
}

class Mul extends Insn {
    public Mul(Cond cond, boolean S, int Rd, int Rn, int Rm) {
        super.op = mul;
        super.cond = cond;
        super.S = S;

        //mul rd,rm,rs clobbered
        super.Rd = Rd;
        super.Rn = Rn;
        super.Rm = Rm;
    }
}

class Label extends Insn {
    public Label(int num) {
        super.num = num;
    }
}