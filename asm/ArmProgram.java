package asm;

import java.util.ArrayList;

import analysis.BasicBlocks;
import ir3.*;

public class ArmProgram {
    public ArrayList<String> data;
    public ArrayList<BasicBlocks> code;

    public static int temp = 0;

    public ArmProgram() {
        temp = 0;
        data = new ArrayList<>();
        code = new ArrayList<>();
    }

    public void add(BasicBlocks bbs) {
        code.add(bbs);
    }
}
