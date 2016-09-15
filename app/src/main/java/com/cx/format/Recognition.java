package com.cx.format;


import com.cx.cluster.Cluster;
import com.cx.source.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 识别图像
 * Created by CX on 2016/9/1.
 */
public class Recognition {
    private static final int DEFAULT_COLOR_NUM = 16;
    private Cluster cluster;
    private int colorNum;

    public Recognition() {
        new Recognition(DEFAULT_COLOR_NUM);
    }

    public Recognition(int colorNum) {
        new Recognition(colorNum, null);
    }

    public int getColorNum() {
        return colorNum;
    }

    public Recognition(int colorNum, Cluster cluster) {
        this.colorNum = colorNum;
        this.cluster = cluster;
    }


    public Color[] recognition(Color[] colors, int len) {
        if (colors.length < colorNum) return colors;
        clearRedundancyData(colors, len);
        formateColor(colors);
        return gainColorByCluster(colors, 16);
    }

    private Color[] clearNullData(Color[] colors) {
        List<Color> list = new ArrayList<>();
        for (Color c : colors) {
            if (c != null) {
                list.add(c);
                c = null;
            }
        }
        colors = null;
        Color[] color = new Color[list.size()];
        for (int i = 0; i < list.size(); i++) {
            color[i] = list.get(i);
        }
        return color;
    }


    private Color[] gainColorByCluster(Color[] colors, int colorNum) {
        DataBox[] db = new DataBox[colorNum];
        initDataBox(colors, db, colorNum);
        return doCluster(db);
    }

    private Color[] doCluster(DataBox[] db) {
        List<Color> dataList = new ArrayList<>();
        List<Color> tempDataList;
        for (DataBox aDb : db) {
            if (aDb.getSize() > 0) {
                List<Color> tempList = aDb.getData();
                if (cluster != null) {
                    tempDataList = cluster.cluster(tempList);
                    dataList.addAll(tempDataList);
                    tempDataList.clear();
                    tempList.clear();
                }
            }
        }
        db = null;
        Color[] data = new Color[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        return data;
    }

    private void initDataBox(Color[] colors, DataBox[] db, int colorNum) {
        double dx = 0.5 / (db.length - 1);
        for (int i = 0; i < db.length; i++) {
            db[i] = new DataBox();
        }
        for (Color c : colors) {
            if (c != null) {
                int postion = (int) ((c.getDiffer() - 0.5) / dx);
                db[postion].add(c);
            }
        }
        colors = null;
    }


    private static void formateColor(Color[] colors) {
        int[] rgb = new int[3];
        int[] line = new int[3];
        double differ;
        for (Color c : colors) {
            if (c != null) {
                setRGB(c.getColor(), rgb);
                int v_Angle, h_Angle;
                v_Angle = getv_Angle(rgb);
                h_Angle = geth_Angle(rgb);
                c.setvAngle(v_Angle);
                c.sethAngle(h_Angle);
                Arrays.sort(rgb);
                if ((rgb[0] & rgb[1]) == 0) {
                    if (rgb[1] == 0) {
                        c.setDiffer(1);
                    } else {
                        line[0] = SquaresTable.getValues(rgb[1]);
                        line[1] = SquaresTable.getValues(rgb[2]);
                        line[2] = getThirdSidePow(rgb[1], rgb[2]);
                        differ = getDiffer(line);
                        c.setDiffer(differ);
                    }
                } else {
                    line[0] = getThirdSidePow(rgb[0], rgb[1]);
                    line[1] = getThirdSidePow(rgb[0], rgb[2]);
                    line[2] = getThirdSidePow(rgb[1], rgb[2]);
                    differ = getDiffer(line);
                    c.setDiffer(differ);
                }
            }
        }
    }

    private static int geth_Angle(int[] rgb) {
        if (rgb[0] == 0) return 90;
        if (rgb[1] == 0) return 0;
        double v = rgb[1] / rgb[0];
        return (int) Math.toDegrees(Math.atan(v));
    }

    private static int getv_Angle(int[] rgb) {
        if (rgb[2] == 0) return 0;
        double a, v;
        int a2 = SquaresTable.getValues(rgb[0]) + SquaresTable.getValues(rgb[1]);
        a = Math.sqrt(a2);
        v = rgb[2] / a;
        return (int) Math.toDegrees(Math.atan(v));
    }


    private static double getDiffer(int[] line) {
        double b, c, dividend, divisor;
        b = Math.sqrt(line[1]);
        c = Math.sqrt(line[2]);
        dividend = line[1] + line[2] - line[0];
        divisor = 2 * b * c;
        return dividend / divisor;
    }

    /**
     * 两边及其夹角120度，求第三边的平方
     */
    private static int getThirdSidePow(int a, int b) {
        int a2, b2, c2;
        a2 = SquaresTable.getValues(a);
        b2 = SquaresTable.getValues(b);
        c2 = a2 + b2 + a * b;
        return c2;
    }


    private static void setRGB(int color, int[] rgb) {
        rgb[0] = (color >> 16) & 0xff;
        rgb[1] = (color >> 8) & 0xff;
        rgb[2] = color & 0xff;
    }


    /**
     * 去掉冗余数据
     */
    private static void clearRedundancyData(Color[] colors, int len) {
        Arrays.sort(colors, new Comparator<Color>() {
            @Override
            public int compare(Color lhs, Color rhs) {
                if (lhs.count > rhs.count) return 1;
                else if (lhs.count < rhs.count) return -1;
                else return 0;
            }
        });
        //平衡因子
        int balancePostion = len / colors.length;
        //寻找平衡点
        int spilt = findBalancePostion(colors, balancePostion);
        int end;
        if (spilt > 0) {
            end = spilt / 2;
        } else {
            return;
        }
        for (int i = 0; i < end; i++) {
            colors[i] = null;
        }
    }

    /**
     * 寻找总的像素个数与颜色直方图中颜色种类比值在colors的平衡点
     *
     * @param colors
     * @param balancePostion
     * @return
     */
    private static int findBalancePostion(Color[] colors, int balancePostion) {
        int low = 0;
        int high = colors.length - 1;
        while ((low <= high) && (low <= colors.length - 1) && (high <= colors.length)) {
            int mid = (high + low) >> 1;
            if (balancePostion == colors[mid].count) {
                return mid;
            } else if (balancePostion < colors[mid].count) {
                if (mid > 0)
                    if (balancePostion > colors[mid - 1].count) {
                        return mid;
                    }
                high = mid - 1;
            } else {
                if (mid + 1 < colors.length)
                    if (balancePostion < colors[mid + 1].count) {
                        return mid + 1;
                    }
                low = mid + 1;
            }
        }
        return -1;
    }
}
