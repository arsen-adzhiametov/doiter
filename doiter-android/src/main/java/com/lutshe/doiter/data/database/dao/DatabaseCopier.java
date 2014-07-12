package com.lutshe.doiter.data.database.dao;

import android.content.Context;
import android.util.Log;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.Trace;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.lutshe.doiter.AchievementTimeConstants.DB_RELATED_TAG;
import static com.lutshe.doiter.data.database.dao.DatabaseHelper.DB_NAME;
import static com.lutshe.doiter.data.database.dao.DatabaseHelper.DB_PATH;

/**
 * Created by Arsen Adzhiametov on 06-Jul-14 in IntelliJ IDEA.
 */
@EBean
public class DatabaseCopier {

    @RootContext Context context;

    @Trace(tag = DB_RELATED_TAG, level = Log.INFO)
    public void copy() {
        String fileName = DB_PATH + DB_NAME;
        InputStream input;
        OutputStream output;
        byte[] buffer = new byte[1024];
        int length;
        try {
            input = context.getAssets().open(DB_NAME);
            output = new FileOutputStream(fileName);
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(DB_RELATED_TAG, "database copy error", e);
        }
    }
}
