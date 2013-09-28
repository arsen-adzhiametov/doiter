package com.lutshe.doiter.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.googlecode.androidannotations.annotations.Trace;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EReceiver
public class BootListener extends BroadcastReceiver {

    @Bean
    MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;

    @Override
    @Trace
    public void onReceive(Context context, Intent intent) {
        messagesUpdateAlarmScheduler.scheduleNextAlarm();
    }
}
