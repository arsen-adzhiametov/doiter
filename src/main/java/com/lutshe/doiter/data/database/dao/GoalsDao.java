package com.lutshe.doiter.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Goal;

/**
 * Created by Arturro on 21.08.13.
 */
@EBean
public class GoalsDao {
    static final String GOALS_TABLE = "goals";
    static final String GOAL_ID = "goal_id";
    static final String END_TIME = "end_time";
    static final String GOAL_NAME = "name";

    static final String BUT_ONLY_USER_GOALS = " WHERE " + END_TIME + " is not null";

    static final String SELECT_ALL_GOALS = "SELECT * FROM " + GOALS_TABLE;
    static final String SELECT_USER_GOALS = SELECT_ALL_GOALS + BUT_ONLY_USER_GOALS;

    static final String SELECT_ALL_GOALS_COUNT = "SELECT count(*) FROM " + GOALS_TABLE;
    static final String SELECT_USER_GOALS_COUNT = SELECT_ALL_GOALS_COUNT + BUT_ONLY_USER_GOALS;

    @Bean
    DatabaseHelper db;

    public void updateGoalEndTime(Long goalId, Long endTime) {
        ContentValues values = new ContentValues();
        values.put(END_TIME, endTime);
        db.getWritableDatabase().update(GOALS_TABLE, values, GOAL_ID + "=" + goalId, null);
    }

    public void addGoal(Goal goal) {
        ContentValues values = new ContentValues();
        values.put(GOAL_ID, goal.getId());
        values.put(GOAL_NAME, goal.getName());
        db.getWritableDatabase().insert(GOALS_TABLE, null, values);
    }

    public Goal[] getAllGoals() {
        return getGoals(SELECT_ALL_GOALS);
    }
    public Goal[] getAllUserGoals() {
        return getGoals(SELECT_USER_GOALS);
    }

    private Goal[] getGoals(String query) {
        Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
        Goal[] userGoals = null;
        if (cursor != null && cursor.moveToFirst()) {
            userGoals = new Goal[cursor.getCount()];
            int i = 0;
            do {
                Goal userGoal = mapGoal(cursor);
                userGoals[i++] = userGoal;
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userGoals;
    }

    public int getAllGoalsCount() {
        return getCount(SELECT_ALL_GOALS_COUNT);
    }

    public int getUserGoalsCount() {
        return getCount(SELECT_USER_GOALS_COUNT);
    }

    private int getCount(String query) {
        Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private Goal mapGoal(Cursor cursor) {
        Long goalId = Long.valueOf(cursor.getString(cursor.getColumnIndex(GOAL_ID)));
        Long endTime = Long.valueOf(cursor.getString(cursor.getColumnIndex(END_TIME)));
        String name = cursor.getString(cursor.getColumnIndex(GOAL_NAME));

        Goal goal = new Goal(name, goalId);
        goal.setEndTime(endTime);
        return goal;
    }

    public Goal getGoal(Long goalId) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from " + GOALS_TABLE + " where " + GOAL_ID + " = " + goalId, null);
        try {
            cursor.moveToFirst();
            return mapGoal(cursor);
        } finally {
            cursor.close();
        }
    }
}
