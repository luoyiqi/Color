package com.cx.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

/**
 * Created by MoYe on 2016/4/16.
 */
public class Utils {
    /**
     * 根据屏幕尺寸获取缩放后的图像
     */
    public static Bitmap getSuitBitmap(DisplayMetrics displayMetrics, Resources rs, int id) {
        if (rs == null || displayMetrics == null) return null;
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return getSuitBitmap(width, height, rs, id);
    }
    //根据对应的宽高对图像进行缩放
    public static Bitmap getSuitBitmap(int width, int height, Resources rs, int id) {
        if (rs == null) return null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(rs, id, opt);
        int imageWidth = opt.outWidth;
        int imageHeight = opt.outHeight;
        if (imageHeight < width && imageWidth < height) {
            opt.inSampleSize = 1;
        } else {
            opt.inSampleSize = Math.max(Math.round(imageWidth * 1.0f / width), Math.round(imageHeight * 1.0f / height));
        }
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(rs, id, opt);
    }


}
