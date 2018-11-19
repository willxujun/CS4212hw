package codegen;

import static codegen.MemOp.*;
import static codegen.MemSrc.*;

import java.util.ArrayList;

class Mem extends Access {
    public MemOp operation;
    public MemSrc source;

    private int reg;
    private ArrayList<Integer> spillOffsets;
    private int getOffset;

    private boolean hasField;
    private int fieldReg;
    private ArrayList<Integer> fieldSpills;
    private int fieldOffset;

    private int dataLabel;

    public Mem(int reg, int offset, MemOp op, MemSrc src) {
        this.reg = reg;
        this.getOffset = offset;
        this.hasField = false;
        operation = op;
        source = src;
    }

    //for static
    public Mem(int reg, int dataLabel, MemSrc source) {
        this.reg = reg;
        this.dataLabel = dataLabel;
        this.source = source;
        this.operation = READ;
    }

    public void addSpill(Integer offset) {
        if(spillOffsets == null)
            spillOffsets = new ArrayList<>();
        spillOffsets.add(offset);
    }

    public void addFieldAccess(Access access) {
        Mem memAccess = (Mem)access;
        hasField = true;
        source = HEAP;
        fieldReg = memAccess.getReg();
        fieldSpills = memAccess.getSpillOffsets();
        fieldOffset = memAccess.getGetOffset();
    }

    public int getReg() {
        return reg;
    }

    public ArrayList<Integer> getSpillOffsets() {
        return spillOffsets;
    }

    public int getGetOffset() {
        return getOffset;
    }

    public void setGetOffset(int getOffset) {
        this.getOffset = getOffset;
    }

    public void setOp(MemOp op) {
        operation = op;
    }
}
