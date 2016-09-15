package com.cx.format;

/**
 * Created by MoYe on 2016/7/4.
 */
public class SquaresTable {
    private static final int[]table=new int[256];

    private SquaresTable(){}

    public static int getValues(int postion){
        if (postion<1||postion>255) return -1;
        if (table[postion]==0){
            table[postion]= (int) Math.pow(postion,2);
        }
        return table[postion];
    }
}
