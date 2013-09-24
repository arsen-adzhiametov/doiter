package com.lutshe.doiter.views.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
}
