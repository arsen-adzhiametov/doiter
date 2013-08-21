package com.lutshe.doiter.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Message;

import org.joda.time.DateTime;

/**
 * Created by Arturro on 21.08.13.
 */
@EBean
public class MessagesDao {
    static final String MESSAGES_TABLE = "messages";
    static final String MESSAGE_ID = "id";
    static final String TEXT = "text";
    static final String DELIVERY_TIME = "delivery_time";
    static final String USER_GOAL_ID = "user_goal_id";
    static final String TYPE = "type";

    static final String SELECT_ALL_MESSAGES = "SELECT * FROM " + MESSAGES_TABLE + " WHERE " + USER_GOAL_ID;
    static final String SELECT_LAST_NOTIFICATION_TIME = "SELECT max(" + DELIVERY_TIME + ") FROM " + MESSAGES_TABLE + " WHERE " + USER_GOAL_ID;

    @Bean
    DatabaseHelper db;

    public void addMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(MESSAGE_ID, message.getId());
        values.put(USER_GOAL_ID, message.getUserGoalId());
        values.put(TEXT, message.getText());
        values.put(DELIVERY_TIME, System.currentTimeMillis());
        values.put(TYPE, message.getType().name());
        db.getWritableDatabase().insert(MESSAGES_TABLE, null, values);
    }

    public Message[] getAllMessages(Long goalId) {
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_ALL_MESSAGES + " = " + goalId, null);
        Message[] messages = null;
        if (cursor != null && cursor.moveToFirst()) {
            messages = new Message[cursor.getCount()];
            int i = 0;
            do {
                Message message = mapMessage(cursor);
                messages[i++] = message;
            } while (cursor.moveToNext());
            cursor.close();
        }
        return messages;
    }

    public Long getLastNotificationTime(long goalId) {
        Long result = null;
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_LAST_NOTIFICATION_TIME + " = " + goalId, null);
        try {
            result = cursor.getLong(0);
        } finally {
            cursor.close();
            return result;
        }
    }

    public Message getMessage(Long goalId, Message.Type type) {
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_ALL_MESSAGES + " and " + TYPE + " = '" + type.name() + "'", null);
        try {
            cursor.moveToFirst();
            return mapMessage(cursor);
        } finally {
            cursor.close();
        }
    }

    private Message mapMessage(Cursor cursor) {
        Long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(MESSAGE_ID)));
        Long goalId = Long.valueOf(cursor.getString(cursor.getColumnIndex(USER_GOAL_ID)));
        String text = cursor.getString(cursor.getColumnIndex(TEXT));
        Long deliveryTime = Long.valueOf(cursor.getString(cursor.getColumnIndex(DELIVERY_TIME)));
        Message.Type type = Message.Type.valueOf(cursor.getString(cursor.getColumnIndex(TYPE)));

        Message message = new Message(id, text, goalId);
        message.setDeliveryTime(deliveryTime);
        message.setType(type);
        return message;
    }

    public void updateMessageDeliveryTime(Long messageId) {
        ContentValues values = new ContentValues();
        values.put(DELIVERY_TIME, DateTime.now().getMillis());
        db.getWritableDatabase().update(MESSAGES_TABLE, values, MESSAGE_ID + "=" + messageId, null);
    }
}
