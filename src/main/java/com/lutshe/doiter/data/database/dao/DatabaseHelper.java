package com.lutshe.doiter.data.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

import static com.lutshe.doiter.data.database.dao.GoalsDao.END_TIME;
import static com.lutshe.doiter.data.database.dao.GoalsDao.GOALS_TABLE;
import static com.lutshe.doiter.data.database.dao.GoalsDao.GOAL_ID;
import static com.lutshe.doiter.data.database.dao.GoalsDao.GOAL_NAME;
import static com.lutshe.doiter.data.database.dao.MessagesDao.DELIVERY_TIME;
import static com.lutshe.doiter.data.database.dao.MessagesDao.MESSAGES_TABLE;
import static com.lutshe.doiter.data.database.dao.MessagesDao.MESSAGE_ID;
import static com.lutshe.doiter.data.database.dao.MessagesDao.TEXT;
import static com.lutshe.doiter.data.database.dao.MessagesDao.TYPE;
import static com.lutshe.doiter.data.database.dao.MessagesDao.USER_GOAL_ID;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean(scope = Scope.Singleton)
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "doiterDB";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GOALS_TABLE + " (" +
                    GOAL_ID + " INTEGER PRIMARY KEY, " +
                    END_TIME + " INTEGER," +
                    GOAL_NAME + " TEXT NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE " + MESSAGES_TABLE + " (" +
                    MESSAGE_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    TEXT + " TEXT NOT NULL, " +
                    DELIVERY_TIME + " LONG , " +
                    USER_GOAL_ID + " INTEGER NOT NULL , " +
                    TYPE + " TEXT NOT NULL DEFAULT 'OTHER', " +

                    "FOREIGN KEY (" + USER_GOAL_ID + ") REFERENCES " + GOALS_TABLE + " (" + GOAL_ID + ")" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GOALS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
        onCreate(db);
    }
}
