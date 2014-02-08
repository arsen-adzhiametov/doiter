package com.lutshe.doiter.views.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @Author: Art
 */
public final class BitmapUtils {

    public static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap getBitmapScaledToHeight(Resources res, int height, int resId) {
        int[] sizeData = getImageSize(res, resId);

        float ratio = (float) height / (float) sizeData[0];
        float width = ratio * (float) sizeData[1];

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(sizeData[0], sizeData[1], (int) width, height);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) width, height, true);
        bitmap.recycle();
        return scaledBitmap;
    }

    public static ScaleProperties fillScaleProperties(Resources res, int resId, int desiredHeight) {
        ScaleProperties properties = new ScaleProperties();

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        properties.ratio = (float) desiredHeight / options.outHeight;
        properties.options = options;
        properties.options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, 0, desiredHeight);
        properties.options.inJustDecodeBounds = false;
        properties.options.inPreferQualityOverSpeed = true;

        return properties;
    }

    public static Bitmap getBitmapScaledToHeight(Resources res, int resId, ScaleProperties props) {
        int[] sizeData = getImageSize(res, resId);
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, props.options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (sizeData[0] * props.ratio), (int) (sizeData[1] * props.ratio), true);

        bitmap.recycle();
        return scaledBitmap;
    }

    public static int[] getImageSize(Resources res, int resId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        return new int[] {options.outWidth, options.outHeight};
    }
}
