package com.lutshe.doiter.preloaders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

/**
 * Created by Arturro on 24.08.13.
 */
public class AppListener implements WakefulIntentService.AlarmListener {
    @Override
    public void scheduleAlarms(AlarmManager alarmManager, PendingIntent pendingIntent, Context context) {
        Log.i("Loaders alarm", "scheduling");
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10000,//AlarmManager.INTERVAL_HOUR,
//                AlarmManager.INTERVAL_DAY,
                10000,
                pendingIntent);
    }

    @Override
    public void sendWakefulWork(Context context) {
        WakefulIntentService.sendWakefulWork(context, LoaderService_.class);
    }

    @Override
    public long getMaxAge() {
        return AlarmManager.INTERVAL_DAY * 2;
    }
}
