package com.lutshe.doiter.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EBean
public class MessagesUpdateAlarmScheduler {

    private static final int HOUR_OF_DAY = 14;
    private static final long TIME_INTERVAL = 24L * 60 * 60 * 1000;  //one day in millis
//    private static final long TIME_INTERVAL = 5L * 60 * 1000;  //5 minutes in millis

    @RootContext Context context;
    @SystemService AlarmManager alarmManager;
    @Bean MessagesDao messagesDao;

    @Trace
    public void scheduleAlarmIfNotSet() {
        if (!isAlarmAlreadySet()) {
            DateTime nextNotificationTime = DateTime.now().withTimeAtStartOfDay().withHourOfDay(HOUR_OF_DAY);
            PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    nextNotificationTime.getMillis(),
                    TIME_INTERVAL,
                    pendingIntent);
            Log.d("lutshe.alarm", "Alarm scheduled on " + nextNotificationTime + " with interval " +
                    TIME_INTERVAL + "ms");
        }
    }

    private boolean isAlarmAlreadySet(){
        boolean alarmUp = getPendingIntent(PendingIntent.FLAG_NO_CREATE) != null;
        if (alarmUp) {
            Log.d("lutshe.alarm", "Alarm is already active");
        }
        return alarmUp;
    }

    private PendingIntent getPendingIntent(int flag) {
        Intent intent = new Intent(context, MessagesUpdateAlarmListener_.class);
        return PendingIntent.getBroadcast(context, 0, intent, flag);
    }

}
