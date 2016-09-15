package com.cx.cluster;


import com.cx.source.Color;

import java.util.List;

/**
 * 聚类算法的接口
 * Created by ChenXiao on 2016/9/1.
 */
 public interface Cluster {
    List<Color>cluster(List<Color>data);

}
