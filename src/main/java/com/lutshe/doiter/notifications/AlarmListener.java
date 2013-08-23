package com.lutshe.doiter.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EReceiver
public class AlarmListener extends BroadcastReceiver {

    @SystemService
    NotificationManager notificationManager;

    @Bean
    NotificationFactory notificationFactory;

    @Bean
    MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;

    @Bean
    MessagesDao messagesDao;

    @Bean
    GoalsDao goalsDao;

    @Override
    public void onReceive(Context context, Intent intent) {
        int quantity = deliverMessages();
        sendNotification(quantity);
        scheduleNext();
    }

    private int deliverMessages() {
        Goal[] userGoals = goalsDao.getActiveUserGoals();
        for (Goal goal : userGoals) {
            DateTime goalEndTime = new DateTime(goal.getEndTime());
            if (isGoalGoesToEnd(goalEndTime)) {
                updateData(goal, Message.Type.LAST);
            } else {
                updateData(goal, Message.Type.OTHER);
            }
        }
        return userGoals.length;
    }

    private void updateData(Goal goal, Message.Type type) {
        Message message = messagesDao.getMessage(goal.getId(), type);
        messagesDao.updateMessageDeliveryTime(message.getId());
        if (type== Message.Type.LAST) goalsDao.updateGoalStatus(goal.getId(), Goal.Status.INACTIVE);
    }

    private boolean isGoalGoesToEnd(DateTime goalEndTime) {
        return goalEndTime.getDayOfYear() == DateTime.now().getDayOfYear();
    }

    private void sendNotification(int quantity) {
        Notification notification = notificationFactory.createNotification(quantity);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }

    private void scheduleNext() {
        messagesUpdateAlarmScheduler.scheduleNextAlarm();
    }
}
