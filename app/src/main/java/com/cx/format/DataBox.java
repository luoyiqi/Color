package com.cx.format;


import com.cx.source.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiao on 2016/9/4.
 */
public class DataBox {
    private List<Color> list = new ArrayList<>();

    public void add(Color color) {
        list.add(color);
    }

    public List<Color> getData() {
        if (list.size() < 0) return null;
        return list;
    }
   public void clear(){
       list.clear();
   }
    public int getSize() {
        return list.size();
    }

}
