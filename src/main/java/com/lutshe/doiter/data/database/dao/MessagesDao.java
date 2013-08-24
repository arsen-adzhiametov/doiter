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
    static final String ORDER_INDEX = "order_index";

    static final String SELECT_ALL_MESSAGES = "SELECT * FROM " + MESSAGES_TABLE + " WHERE " + USER_GOAL_ID;
    static final String SELECT_LAST_NOTIFICATION_TIME = "SELECT max(" + DELIVERY_TIME + ") FROM " + MESSAGES_TABLE;

    @Bean
    DatabaseHelper db;

    public void addMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(MESSAGE_ID, message.getId());
        values.put(USER_GOAL_ID, message.getUserGoalId());
        values.put(TEXT, message.getText());
        values.put(TYPE, message.getType().name());
        values.put(ORDER_INDEX, message.getOrderIndex());
        db.getWritableDatabase().insert(MESSAGES_TABLE, null, values);
    }

    public Message[] getAllMessages(Long goalId) {
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_ALL_MESSAGES + " = " + goalId + " AND " + DELIVERY_TIME + " IS NOT NULL", null);
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

    public Long getLastNotificationTime() {
        Long result = null;
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_LAST_NOTIFICATION_TIME, null);
        try {
            result = cursor.getLong(0);
        } finally {
            cursor.close();
            return result;
        }
    }

    public Message getMessage(Long goalId, Message.Type type) {
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_ALL_MESSAGES + " = " + goalId + " and " + TYPE + " = '" + type.name() + "'", null);
        try {
            cursor.moveToFirst();
            return mapMessage(cursor);
        } finally {
            cursor.close();
        }
    }

    public Message getMessage(Long goalId, Long messageIndex) {
        Cursor cursor = db.getReadableDatabase().rawQuery(SELECT_ALL_MESSAGES + " = " + goalId + " and " + ORDER_INDEX + " = " + messageIndex, null);
        try {
            cursor.moveToFirst();
            return mapMessage(cursor);
        } finally {
            cursor.close();
        }
    }

    private Message mapMessage(Cursor cursor) {
        Message message = null;
        try {
            Long id = cursor.getLong(cursor.getColumnIndex(MESSAGE_ID));
            Long goalId = cursor.getLong(cursor.getColumnIndex(USER_GOAL_ID));
            String text = cursor.getString(cursor.getColumnIndex(TEXT));
            Long deliveryTime = cursor.getLong(cursor.getColumnIndex(DELIVERY_TIME));
            Long orderIndex = cursor.getLong(cursor.getColumnIndex(ORDER_INDEX));
            Message.Type type = Message.Type.valueOf(cursor.getString(cursor.getColumnIndex(TYPE)));

            message = new Message(id, text, goalId, orderIndex);
            message.setDeliveryTime(deliveryTime);
            message.setType(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public void updateMessageDeliveryTime(Long messageId) {
        ContentValues values = new ContentValues();
        values.put(DELIVERY_TIME, DateTime.now().getMillis());
        db.getWritableDatabase().update(MESSAGES_TABLE, values, MESSAGE_ID + "=" + messageId, null);
    }
}