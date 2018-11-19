package codegen;

import java.util.ArrayList;
import java.util.HashMap;

import analysis.BasicBlocks;
import analysis.Block;
import ir3.*;
import asm.*;

//generates BBs
public class CodeGen {
    ArrayList<BasicBlocks> bbss;
    ArmProgram arm;

    //utilities for whole prog
    private static ClassOffsetMap offsetMap;
    private static HashMap<BasicBlocks, MethodData> methodDataMap = new HashMap<>();
    //utilities for procedure
    private static RDAD regAddrDescriptor;
    private static MethodData methodData;

    public CodeGen(IR3 ir3) {
        bbss = new ArrayList<>();
        ArrayList<Instruction> mCode;
        BasicBlocks bbs;
        for(MethodData m: ir3.mData) {
            mCode = m.code;
            bbs = new BasicBlocks(mCode);
            bbss.add(bbs);
            methodDataMap.put(bbs, m);
        }

        offsetMap = new ClassOffsetMap(ir3.cData);

        //actions to generate code
        arm = new ArmProgram();
        BasicBlocks armProc;
        for(BasicBlocks bbs: bbss) {
            methodData = methodDataMap.get(bbs);
            methodData.preProcessDecls();
            regAddrDescriptor = new RDAD(methodData.getFormals(), methodData.getLocals(), methodData.getTemps(), methodData);
            armProc = generate(bbs);
            arm.add(armProc);
        }
    }

    private static ArrayList<Insn> processSingleInstruction(Instruction ir3ins) {
        Arg3 arg1, arg2, res;
        ArrayList<Arg3> variables;
        Access access;
        Integer potentialReg;

        arg1 = ir3ins.getArg1();
        arg2 = ir3ins.getArg2();
        res = ir3ins.getResult();

        switch(ir3ins.op) {
        case ASSIGN:
            if(arg1 instanceof Access3) {
                processVA((Var3)res, (Access3) arg1);
            } else if(res instanceof Access3){
                processAV((Access3)res, (Var3) arg1);
            }
        case ADD:
        case SUB:
        case LE:
        case GE:
        case LT:
        case GT:
        case NE:
        case EQ:
            //not null, Var3 arguments
            processVVV((Var3)res, (Var3)arg1, (Var3)arg2);
        }

    }

    public static void processVA(Var3 res, Access3 arg1) {
        Access access1 = regAddrDescriptor.getRegForAccess(res, arg1);
        Access accessRes = regAddrDescriptor.getReg(res, null, null, 1);
    }

    public static void processAV(Access3 res, Var3 arg1) {
        Access access1 = regAddrDescriptor.getReg(null, arg1, null, 2);
        Access accessRes = regAddrDescriptor.getRegForAccess(null, res);
    }

    private static void processVVV(Var3 res, Var3 arg1, Var3 arg2) {
        Access access1 = regAddrDescriptor.getReg(res, arg1, arg2, 2);
        Access access2 = regAddrDescriptor.getReg(res, arg1, arg2, 3);
        Access accessRes = regAddrDescriptor.getReg(res, arg1, arg2, 1);
    }

    private static BasicBlocks generate(BasicBlocks ir3Proc) {
        ArrayList<Insn> armCode = new ArrayList<>();

        for(Block b : ir3Proc.blocks) {
            ArrayList<Insn> armInstructions;
            for(Instruction ir3ins: b.ir3Code) {
                armInstructions = processSingleInstruction(ir3ins);
                armCode.addAll(armInstructions);
            }
            b.armCode = armCode;
            b.ir3Code = null;
        }

        return ir3Proc;
    }

    public void showBlocks() {
        for(BasicBlocks bbs : bbss) {
            bbs.show();
        }
    }
}
