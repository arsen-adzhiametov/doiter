package com.lutshe.doiter.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.AlarmListener_;

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

    public void scheduleNotification(Long goalId){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        int id = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, AlarmListener_.class);
        intent.putExtra("goalId", goalId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
