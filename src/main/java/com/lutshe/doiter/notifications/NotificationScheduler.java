package com.lutshe.doiter.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;

import java.util.Calendar;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class NotificationScheduler {

    @RootContext
    Context context;

    @SystemService
    AlarmManager alarmManager;

    public void scheduleNotification(Long goalId) {
        long time = getNextNotificationTime();
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

    private long getNextNotificationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);
        return calendar.getTimeInMillis();
    }

}
