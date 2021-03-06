package com.lutshe.doiter.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.Trace;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EReceiver
public class BootListener extends BroadcastReceiver {

    @Bean
    MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;

    @Override
    @Trace
    public void onReceive(Context context, Intent intent) {
        Log.d("lutshe.alarm", "boot_completed received. Rescheduling alarm");
        messagesUpdateAlarmScheduler.scheduleAlarmIfNotSet();
    }
}
