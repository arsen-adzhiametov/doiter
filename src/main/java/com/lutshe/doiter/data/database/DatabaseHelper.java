package com.lutshe.doiter.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.lutshe.doiter.data.model.UserGoal;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean(scope = Scope.Singleton)
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "doiterDB";

    static final String USER_GOAL_TABLE = "user_goal";
    static final String GOAL_ID = "goal_id";
    static final String END_TIME = "end_time";

    static final String MESSAGE_TABLE = "message";
    static final String MESSAGE_ID = "id";
    static final String TEXT = "text";
    static final String USER_GOAL_ID = "user_goal_id";

    static final String SELECT_ALL = "SELECT  * FROM " + USER_GOAL_TABLE;
    static final String SELECT_COUNT = "SELECT  count(*) FROM " + USER_GOAL_TABLE;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_GOAL_TABLE + " (" +
                GOAL_ID + " INTEGER PRIMARY KEY , " +
                END_TIME + " INTEGER NOT NULL);");
        db.execSQL("CREATE TABLE " + MESSAGE_TABLE + " (" +
                MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TEXT + " TEXT, " +
                USER_GOAL_ID + " INTEGER NOT NULL , FOREIGN KEY (" +
                USER_GOAL_ID + ") REFERENCES " +
                USER_GOAL_TABLE + " (" +
                GOAL_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
        onCreate(db);
    }

    public void addGoal(Long goalId, Long endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_ID, goalId);
        values.put(END_TIME, endTime);
        db.insert(USER_GOAL_TABLE, null, values);
    }

    public UserGoal getUserGoal(Long goalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                USER_GOAL_TABLE,
                new String[]{GOAL_ID, END_TIME},
                GOAL_ID + "=?",
                new String[]{String.valueOf(goalId)},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            UserGoal userGoal = mapUserGoal(cursor);
            cursor.close();
            return userGoal;
        }
        return null;
    }

    public  UserGoal[] getAllUserGoals() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);
        UserGoal[] userGoals = null;
        if (cursor != null && cursor.moveToFirst()) {
            userGoals = new UserGoal[cursor.getCount()];
            int i = 0;
            do {
                UserGoal userGoal = mapUserGoal(cursor);
                userGoals[i++] = userGoal;
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userGoals;
    }

    public int getUserGoalsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_COUNT, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private UserGoal mapUserGoal(Cursor cursor) {
        Long goalId = Long.valueOf(cursor.getString(cursor.getColumnIndex(GOAL_ID)));
        Long endTime = Long.valueOf(cursor.getString(cursor.getColumnIndex(END_TIME)));
        UserGoal userGoal = new UserGoal(goalId, endTime);
        return userGoal;
    }

}
