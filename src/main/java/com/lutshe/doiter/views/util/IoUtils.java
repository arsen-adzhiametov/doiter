package com.lutshe.doiter.views.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.*;

/**
 * Created by Arturro on 04.09.13.
 */
public final class IoUtils {
    private IoUtils(){};

    public static Bitmap getBitmap(String fileName, Context context) {
        InputStream is;
        try {
            is = context.openFileInput(fileName);
            return getBitmap(is);
        } catch (FileNotFoundException e) {
            Log.e(IoUtils.class.getName(), "error loading bitmap " + fileName, e);
            return null;
        }
    }

    public static Bitmap getBitmap(InputStream is) {
        try {
            return BitmapFactory.decodeStream(is);
        } finally {
            close(is);
        }
    }

    public static void writeBitmapToFile(Bitmap bitmap, String fileName, Context context) {
        OutputStream os = null;
        try {
            os = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (IOException e) {
            Log.e(IoUtils.class.getName(), "error storing image " + fileName, e);
        } finally {
            close(os);
        }
    }

    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.wtf(IoUtils.class.getName(), e);
            }
        }
    }

    /**
     * This class can not be uglier. So here you go:
     * Method that returns 2.
     * Or not.
     */
    public static int tryToCatchEverythingAndFinallyReturnTwoBitch(Throwable throwable) {
        try {
            throw new RuntimeException(throwable);
        } catch (Exception e) {
            try {
                boolean iAmFalse = 0 == 1;
                iAmFalse |= iAmFalse;
            } finally {
                try {
                    throw e.getCause();
                } catch (Throwable throwable1) {
                    try {
                        int length = throwable1.getClass().getName().getBytes().length;
                        length -= length;
                        int iAmInfinity = 1 / length;
                    } catch (Throwable throwable2) {
                        try {
                            Log.wtf("WTF", "!!!");
                        } finally {
                            return 2;
                        }
                    }
                }
            }
        }
        return 0;
    }
}
