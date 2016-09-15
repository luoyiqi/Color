package com.cx.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Arrays;

/**
 * 用于处理图片
 * Created by MoYe on 2016/4/17.
 */
public class ScaleImage {
    private static final int CALCULATE_BITMAP_MIN_DIMENSION = 100;

    /**
     * 检查Bitmap是否可用
     *
     * @param bitmap
     */
    private static void checkBitmapParam(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("bitmap can not be null");
        }
        if (bitmap.isRecycled()) {
            throw new IllegalArgumentException("bitmap can not be recycled");
        }
    }

    /**
     * 压缩图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap scaleBitmapDown(Bitmap bitmap) {
        checkBitmapParam(bitmap);
        final int minDimension = Math.min(bitmap.getWidth(), bitmap.getHeight());

        if (minDimension <= CALCULATE_BITMAP_MIN_DIMENSION) {
            // If the bitmap is small enough already, just return it
            return bitmap;
        }

        final float scaleRatio = CALCULATE_BITMAP_MIN_DIMENSION / (float) minDimension;
        return Bitmap.createScaledBitmap(bitmap,
                Math.round(bitmap.getWidth() * scaleRatio),
                Math.round(bitmap.getHeight() * scaleRatio),
                false);
    }

    public static int[] getScalePixels(Bitmap bm) {
        Bitmap sb = scaleBitmapDown(bm);
        int[] pixels = new int[sb.getWidth() * sb.getHeight()];
        bm.getPixels(pixels, 0, sb.getWidth(), 0, 0, sb.getWidth(), sb.getHeight());
        // 如果创建新图片，回收它
        if (sb != bm) {
            sb.recycle();
        }
        return pixels;
    }
    public static int[] getPixels(Bitmap bm) {
        final int[] pixels = new int[bm.getWidth() * bm.getHeight()];
        bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());
        return pixels;
    }
}
