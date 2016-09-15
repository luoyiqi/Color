package com.cx.format;


import com.cx.source.Color;

/**
 * Created by ChenXiao on 2016/9/1.
 */
public class Box {
    private Color color;
    private int currentPopution;
    private int popution;
    private double currentH;
    private double currentV;
    private int count;
    private int h;
    private int v;

    private boolean flag = false;

    public Box() {

    }

    public Color getColor() {
        return color;
    }

    public double getCurrentH() {
        return currentH;
    }

    public double getCurrentV() {
        return currentV;
    }

    public void add(Color color) {
        if (!flag) {
            this.color=color;
            this.currentPopution=color.count;
            this.popution=this.currentPopution;
            this.currentV =  color.getvAngle();
            this.currentH =  color.gethAngle();
            this.v = (int) this.currentV;
            this.h = (int) this.currentH;
            this.count=1;
        }
        count++;
        this.v += color.getvAngle();
        this.h += color.gethAngle();
        this.currentV = color.getvAngle()/count;
        this.currentH = color.gethAngle()/count;
        this.popution+=color.count;
        if (color.count>this.currentPopution){
            this.color=color;
            this.color.count=popution;
            this.currentPopution=this.color.count;
        }
    }
}
