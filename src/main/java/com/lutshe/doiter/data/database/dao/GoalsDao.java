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
    static final String GOAL_STATUS = "status";
    static final String LAST_MESSAGE_INDEX = "last_message_index";

    static final String BUT_ONLY_USER_GOALS = " WHERE " + END_TIME + " is not null";
    static final String BUT_ONLY_ACTIVE_GOALS = " WHERE " + GOAL_STATUS + " = 'ACTIVE'";

    static final String SELECT_ALL_GOALS = "SELECT * FROM " + GOALS_TABLE;
    static final String SELECT_USER_GOALS = SELECT_ALL_GOALS + BUT_ONLY_USER_GOALS;
    static final String SELECT_ACTIVE_GOALS = SELECT_ALL_GOALS + BUT_ONLY_ACTIVE_GOALS;

    static final String SELECT_ALL_GOALS_COUNT = "SELECT count(*) FROM " + GOALS_TABLE;
    static final String SELECT_USER_GOALS_COUNT = SELECT_ALL_GOALS_COUNT + BUT_ONLY_USER_GOALS;

    @Bean
    DatabaseHelper db;

    public void updateGoalEndTime(Long goalId, Long endTime) {
        ContentValues values = new ContentValues();
        values.put(END_TIME, endTime);
        db.getWritableDatabase().update(GOALS_TABLE, values, GOAL_ID + "=" + goalId, null);
    }

    public void updateGoalStatus(Long goalId, Goal.Status status) {
        ContentValues values = new ContentValues();
        values.put(GOAL_STATUS, status.name());
        db.getWritableDatabase().update(GOALS_TABLE, values, GOAL_ID + "=" + goalId, null);
    }

    public void updateGoalLastMessage(Long goalId, long lastMessageIndex) {
        ContentValues values = new ContentValues();
        values.put(LAST_MESSAGE_INDEX, lastMessageIndex);
        db.getWritableDatabase().update(GOALS_TABLE, values, GOAL_ID + "=" + goalId, null);
    }

    public void addGoal(Goal goal) {
        ContentValues values = new ContentValues();
        values.put(GOAL_ID, goal.getId());
        values.put(GOAL_NAME, goal.getName());
        values.put(LAST_MESSAGE_INDEX, goal.getLastMessageIndex());
        db.getWritableDatabase().insert(GOALS_TABLE, null, values);
    }

    public Goal[] getAllGoals() {
        return getGoals(SELECT_ALL_GOALS);
    }
    public Goal[] getAllUserGoals() {
        return getGoals(SELECT_USER_GOALS);
    }
    public Goal[] getActiveUserGoals() {
        return getGoals(SELECT_ACTIVE_GOALS);
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
        Long goalId = cursor.getLong(cursor.getColumnIndex(GOAL_ID));
        String name = cursor.getString(cursor.getColumnIndex(GOAL_NAME));
        Long endTime = cursor.getLong(cursor.getColumnIndex(END_TIME));
        Goal.Status status = Goal.Status.valueOf(cursor.getString(cursor.getColumnIndex(GOAL_STATUS)));
        Long lastMessageIndex = cursor.getLong(cursor.getColumnIndex(LAST_MESSAGE_INDEX));
        Goal goal = new Goal(name, goalId);
        goal.setEndTime(endTime);
        goal.setStatus(status);
        goal.setLastMessageIndex(lastMessageIndex);
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
