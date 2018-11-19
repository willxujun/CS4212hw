package analysis;

import Graph.*;
import asm.Insn;
import ir3.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

//generate BBs only for used procedures, starting from main
public class BasicBlocks extends Graph {
    public HashMap<Integer, Block> blockMap;
    public ArrayList<Block> blocks;

    private Block block;
    public BasicBlocks(ArrayList<Instruction> l) {
        ListIterator<Instruction> itr = l.listIterator();
        blocks = new ArrayList<>();
        makeBlocks(itr);
        registerBlocks();
        populatePredAndSuccInfo();
        entry = blocks.get(0);
        exit = blocks.get(blocks.size() - 1);
    }

    public void toArm() {
        for(Block b: blocks) {
            b.toArm();
        }
    }

    public void registerBlocks() {
        blockMap = new HashMap<>();
        for(Block b: blocks) {
            blockMap.put(b.getLabel(), b);
        }
    }

    public void populatePredAndSuccInfo() {
        Block b, succ;
        ListIterator<Block> l = blocks.listIterator();
        while(l.hasNext()) {
            b = l.next();
            if(l.hasNext()) {
                succ = l.next();
                l.previous();
                b.addSucc(succ);
                succ.addPred(b);
            }
            if(b.getJumpTo() != -1) {
                succ = blockMap.get(b.getJumpTo());
                b.addSucc(succ);
                succ.addPred(b);
            }
        }
    }

    public void makeBlocks(ListIterator<Instruction> l) {
        if(!l.hasNext())
            return;
        Instruction i = l.next();
        //start a block
        if(i instanceof Label3) {
            block = new Block(((Label3) i).getN());
            block.add(i);
            System.out.println("Starting at " + i);
            completeBlocks(l);
        } else {
            block = new Block((new Label3()).getN());
            block.add(i);
            System.out.println("Starting at " + i);
            completeBlocks(l);
        }
    }

    public void completeBlocks(ListIterator<Instruction> l) {
        if(!l.hasNext()) {
            blocks.add(block);
            return;
        }
        Instruction i = l.next();
        int jumpTo = -1;
        if(i instanceof Jmp3 || i instanceof CJmp3) {
            block.add(i);
            if(i instanceof Jmp3 && i.arg1 instanceof Int3) {
                jumpTo = ((Int3) i.arg1).getVal();
                block.setJumpTo(jumpTo);
            }
            else if(i instanceof CJmp3) {
                if(((CJmp3) i).arg2 instanceof Int3) {
                    jumpTo = ((Int3) ((CJmp3) i).arg2).getVal();
                    block.setJumpTo(jumpTo);
                }
            }
            blocks.add(block);
            System.out.println("Jump block added.");
            makeBlocks(l);
        } else if(i instanceof Label3) {
            l.previous();
            blocks.add(block);
            System.out.println("At " + i +  " Label: block added.");
            makeBlocks(l);
        } else {
            block.add(i);
            completeBlocks(l);
        }
    }

    public void show() {
        System.out.println("----------procedure begins--------");
        for(Block b : blocks) {
            b.show();
        }
    }
}

