package com.lutshe.doiter.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class MessagesUpdateAlarmScheduler {

    private static final long TIME_GAP = 10 * 1000;

    @RootContext
    Context context;

    @SystemService
    AlarmManager alarmManager;

    @Bean
    MessagesDao messagesDao;

    public void scheduleNextAlarm() {
        DateTime nextNotificationTime = getNextNotificationTime();
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotificationTime.getMillis(), pendingIntent);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, AlarmListener_.class);
        return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private DateTime getNextNotificationTime() {
        long lastNotificationTime = getLastNotificationTime();
        DateTime now = DateTime.now();
        DateTime last = now.withMillis(lastNotificationTime);
        DateTime next = last.plus(TIME_GAP);
        return next;
    }

    private long getLastNotificationTime() {
        Long lastNotificationTime = messagesDao.getLastNotificationTime();
        return lastNotificationTime != null ? lastNotificationTime : DateTime.now().getMillis();
    }
}
