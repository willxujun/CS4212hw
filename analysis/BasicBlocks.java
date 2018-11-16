package analysis;

import Graph.Node;
import ir3.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class BasicBlocks {
    public ArrayList<Block> blocks;
    public HashMap<Integer, Block> blockMap;

    private Block block;
    public BasicBlocks(ArrayList<Instruction> l) {
        blocks = new ArrayList<>();
        ListIterator<Instruction> itr = l.listIterator();
        makeBlocks(itr);
        registerBlocks();
    }

    void registerBlocks() {
        blockMap = new HashMap<>();
        for(Block b: blocks) {
            blockMap.put(b.getLabel(), b);
        }
    }

    void makeBlocks(ListIterator<Instruction> l) {
        if(!l.hasNext())
            return;
        Instruction i = l.next();
        //start a block
        if(i instanceof Label3) {
            block = new Block(((Label3) i).getN());
            completeBlocks(l);
        } else {
            block = new Block((new Label3()).getN());
            completeBlocks(l);
        }
    }

    void completeBlocks(ListIterator<Instruction> l) {
        if(!l.hasNext()) {
            blocks.add(block);
            return;
        }
        Instruction i = l.next();
        if(i instanceof Jmp3 || i instanceof CJmp3) {
            block.add(i);
            blocks.add(block);
            makeBlocks(l);
        } else if(i instanceof Label3) {
            l.previous();
            blocks.add(block);
            makeBlocks(l);
        } else {
            block.add(i);
            completeBlocks(l);
        }
    }
}

class Block {
    Integer label;
    ArrayList<Instruction> code;

    public Block() {}

    public Block(int label) {
        this.label = label;
        code = new ArrayList<>();
    }

    /*
    public void setOut1(int out1) {
        this.out1 = out1;
    }

    public void setOut2(int out2) {
        this.out2 = out2;
    }
    */

    public void setCode(ArrayList<Instruction> code) {
        this.code = code;
    }

    public void add(Instruction i) {
        code.add(i);
    }

    public Integer getLabel() {
        return label;
    }


}

