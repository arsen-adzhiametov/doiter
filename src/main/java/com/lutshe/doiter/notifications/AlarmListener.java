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
        if (userGoals == null) return 0;
        for (Goal goal : userGoals) {
            DateTime goalEndTime = new DateTime(goal.getEndTime());
            if (isToday(goalEndTime)) {
                deliverLastMessage(goal);
            } else {
                deliverOtherMessage(goal);
            }
        }
        return userGoals.length;
    }

    private void deliverOtherMessage(Goal goal) {
        long nextMessageIndex = goal.getLastMessageIndex()+1;
        Message message = messagesDao.getMessage(goal.getId(), nextMessageIndex);
        if (message == null) {
            deliverLastMessage(goal);
            return;
        }
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalLastMessage(goal.getId(), nextMessageIndex);
    }

    private void deliverLastMessage(Goal goal) {
        Message message = messagesDao.getMessage(goal.getId(), Message.Type.LAST);
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalStatus(goal.getId(), Goal.Status.INACTIVE);
    }

    private boolean isToday(DateTime goalEndTime) {
        return goalEndTime.getDayOfYear() == DateTime.now().getDayOfYear();
    }

    private void sendNotification(int quantity) {
        if (quantity==0) return;
        Notification notification = notificationFactory.createNotification(quantity);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }

    private void scheduleNext() {
        messagesUpdateAlarmScheduler.scheduleNextAlarm();
    }
}
