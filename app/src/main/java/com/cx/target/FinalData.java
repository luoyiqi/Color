package com.cx.target;


import com.cx.cluster.QuickCluster;
import com.cx.format.Recognition;
import com.cx.source.Color;
import com.cx.source.ColorHistogram;

/**
 * Created by ChenXiao on 2016/9/1.
 */
public class FinalData {
    private int len;

    /**
     * 获取颜色
     * @param pixels 图像的像素值
     * @param len 像素总数
     * @return
     */
    public static int[] getColor(int[] pixels,int len) {
        if (pixels.length!=len) len=pixels.length;
        ColorHistogram colorHistogram = new ColorHistogram();
        Color[]colors=colorHistogram.init(pixels);
        pixels=null;
        QuickCluster dbscan=new QuickCluster();
        Recognition recognition=new Recognition(16, dbscan);
       Color[]data=recognition.recognition(colors,len);
        return  toRGB(data);
    }

    /**
     * 将Color对象转换为RGB
     * @param colors
     * @return
     */
   private static int[] toRGB(Color[]colors){
       int[]rgb=new int[colors.length];
       for (int i = 0; i <colors.length ; i++) {
           if (colors[i]!=null)
           rgb[i]=colors[i].getColor();
           colors[i]=null;
       }
       colors=null;
       return rgb;
   }

}
