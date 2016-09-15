package com.cx.source;


import java.util.Arrays;

/**
 * 颜色直方图
 * Created by ChenXiao on 2016/9/1.
 */
public class ColorHistogram {
    private Histogram histogram;
    public ColorHistogram(){

    }
    public ColorHistogram(Histogram histogram) {
        this.histogram = histogram;
    }

    public Color[] init(int[] pixels) {
        if (histogram != null) {
          return  histogram.init(pixels);
        } else {
         return format(pixels);
        }
    }
    public  Color[] format(int[]pixels){
        Arrays.sort(pixels);
        //寻找颜色值被分成的个数
        int differentColorNum = countColorNum(pixels);
        Color[] colors = new Color[differentColorNum];
        //初步初始化颜色数组
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color();
        }
        initColors(pixels, colors);
        return colors;
    }
    private void initColors(int[] pixels, Color[] colors) {
        int index = 0;
        colors[index].setColor(pixels[index]);
        int count = 0;
        for (int p : pixels) {
            if (colors[index].getColor() == p) {
                count++;
            } else {
                colors[index].count = count;
                index++;
                colors[index].setColor(p);
                count = 1;
            }
        }
        if (colors[index].count == 0) {
            colors[index].count = count;
        }
    }

    private int countColorNum(int[] pixels) {
        int temp = 1, num = 0;
        for (int p : pixels) {
            if (temp != p) {
                temp = p;
                num++;
            }
        }
        return num;
    }
}
