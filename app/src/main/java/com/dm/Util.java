package com.dm;


import java.util.BitSet;

public class Util {

    public static BitSet convert(int[] array) {
        BitSet bs = new BitSet(array.length);
        for (int i=0; i<array.length; ++i) {
            bs.set(i, array[i] == 1);
        }
        return  bs;
    }
}
