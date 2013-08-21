package com.lutshe.doiter.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.MessagesProvider;
import com.lutshe.doiter.data.provider.stub.MessagesProviderStub;

import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class NotificationScheduler {

    private static final long TIME_GAP = 10 * 1000;

    @RootContext
    Context context;

    @SystemService
    AlarmManager alarmManager;

    @Bean
    MessagesDao messagesDao;

    @Bean
    GoalsDao goalsDao;

    @Bean(MessagesProviderStub.class)
    MessagesProvider messagesProvider;

    public void scheduleNextNotification(Long goalId) {
        Goal goal = goalsDao.getGoal(goalId);
        DateTime goalEndTime = new DateTime(goal.getEndTime());
        DateTime nextNotificationTime = getNextNotificationTime(goalId);

        long messageId = messagesProvider.getRandomMessage(goalId).getId();

        if (schedulingLastMessage(goalEndTime, nextNotificationTime)) {
            nextNotificationTime = goalEndTime;
            messageId = messagesDao.getMessage(goalId, Message.Type.LAST).getId();
        }

        PendingIntent pendingIntent = getPendingIntent(goalId, messageId);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotificationTime.getMillis(), pendingIntent);
    }

    private boolean schedulingLastMessage(DateTime goalEndTime, DateTime nextNotificationTime) {
        return goalEndTime.getDayOfYear() == nextNotificationTime.getDayOfYear();
    }

    private PendingIntent getPendingIntent(long goalId, long messageId) {
        int id = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, AlarmListener_.class);

        intent.putExtra("goalId", goalId);
        intent.putExtra("messageId", messageId);

        return PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private DateTime getNextNotificationTime(long goalId) {
        long lastNotificationTime = getLastNotificationTime(goalId);
        DateTime now = DateTime.now();
        DateTime last = now.withMillis(lastNotificationTime);
        DateTime next = last.plus(TIME_GAP);
        return next;
    }

    private long getLastNotificationTime(long goalId){
        Long lastNotificationTime = messagesDao.getLastNotificationTime(goalId);
        return lastNotificationTime != null ? lastNotificationTime : DateTime.now().getMillis();
    }
}
