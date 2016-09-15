package com.cx.source;

/**
 * Created by ChenXiao on 2016/9/1.
 */
public class Color {
    private int color;
    private double differ;
    private int hAngle;
    private int vAngle;
    public int count;
    public boolean flag;

    public Color() {
        flag = true;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public void setDiffer(double differ) {
        this.differ = differ;
    }


    public void setvAngle(int vAngle) {
        this.vAngle = vAngle;
    }

    public void sethAngle(int hAngle) {
        this.hAngle = hAngle;
    }

    public int gethAngle() {
        return hAngle;
    }

    public int getvAngle() {
        return vAngle;
    }

    public double getDiffer() {
        return differ;
    }


    public int getColor() {
        return color;
    }

}
