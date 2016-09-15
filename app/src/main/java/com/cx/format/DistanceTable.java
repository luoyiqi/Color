package com.cx.color.format;

/**
 * Created by ChenXiao on 2016/9/5.
 */
public class DistanceTable {
    private DistanceTable() {
    }
    private static final float[] table = new float[91 * 45];
    public static float getValues(int i, int j) {
        j=Math.abs(j);
        i=Math.abs(i);
        j = Math.max(i, j);
        i = Math.min(i, j);
        if (i==0)return j;
        int postion = (j*j-j)/2+i-1;
        if (table[postion] == 0) {
            table[postion] = (float) Math.sqrt(i*i+j*j);
        }
        return table[postion];
    }
}
