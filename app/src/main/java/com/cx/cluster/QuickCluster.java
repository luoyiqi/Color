package com.cx.cluster;


import com.cx.source.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的聚类
 * Created by ChenXiao on 2016/9/11.
 */
public class QuickCluster implements Cluster {
    private Color[][] table;

    public QuickCluster() {
        table = new Color[6][6];
    }

    @Override
    public List<Color> cluster(List<Color> data) {
        for (Color c : data) {
            int i = c.gethAngle() / 18;
            int j = c.getvAngle() / 18;
            if (table[i][j] != null) {
                int count = table[i][j].count;
                if (table[i][j].count  < c.count) {
                    table[i][j] = c;
                }
                table[i][j].count = count + c.count;
            } else {
                table[i][j] = c;
            }
        }
        List<Color> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Color t = dfs(i, j);
                if (t != null) {
                    list.add(t);
                }
            }

        }
        return list;
    }

    private Color dfs(int x, int y) {
        Color temp = null;
        for (int dx = -1; dx < 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = x + dy;
                if (0 <= nx && nx < 6 && 0 <= ny && ny < 6 && table[nx][ny] != null) {
                    if (temp != null && temp.count < table[nx][ny].count) {
                        temp = table[nx][ny];
                    } else {
                        temp = table[nx][ny];
                    }
                    table[nx][ny] = null;
                    dfs(nx, ny);
                }
            }
        }
        return temp;

    }


}
