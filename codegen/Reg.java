package codegen;

import java.util.ArrayList;

class Reg extends Access {
    int reg;
    private ArrayList<Integer> spillOffsets;

    public Reg(int reg) {
        this.reg = reg;
    }

    public int getReg() {
        return reg;
    }

    public void addSpill(Integer offset) {
        if(spillOffsets == null)
            spillOffsets = new ArrayList<>();
        spillOffsets.add(offset);
    }

}
