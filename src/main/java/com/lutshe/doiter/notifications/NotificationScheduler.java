package com.lutshe.doiter.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.DatabaseHelper;
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
    DatabaseHelper databaseHelper;

    public void scheduleNextNotification(Long goalId) {
        long time = getNextNotificationTime(goalId);
        PendingIntent pendingIntent = getPendingIntent(goalId);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private PendingIntent getPendingIntent(long goalId) {
        int id = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, AlarmListener_.class);
        intent.putExtra("goalId", goalId);
        return PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private long getNextNotificationTime(long goalId) {
        long lastNotificationTime = getLastNotificationTime(goalId);
        DateTime now = DateTime.now();
        DateTime last = now.withMillis(lastNotificationTime);
        DateTime next = last.plus(TIME_GAP);
        return next.getMillis();
    }

    private long getLastNotificationTime(long goalId){
        Long lastNotificationTime = databaseHelper.getLastNotificationTime(goalId);
        return lastNotificationTime != null ? lastNotificationTime : DateTime.now().getMillis();
    }
}