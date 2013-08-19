package com.lutshe.doiter.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.DatabaseHelper;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.MessagesProvider;
import com.lutshe.doiter.data.provider.stub.MessagesProviderStub;

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
    NotificationScheduler notificationScheduler;

    @Bean
    DatabaseHelper databaseHelper;

    @Bean(MessagesProviderStub.class)
    MessagesProvider messagesProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("doiter_on_boot", "message coming");
        Long goalId = (Long)intent.getExtras().get("goalId");
        Message message = messagesProvider.getRandomMessage(goalId);
        databaseHelper.addMessage(message);
        Notification notification = notificationFactory.createNotification(message);
        int notificationId = (int)message.getUserGoalId();
        notificationManager.notify(notificationId, notification);
        notificationScheduler.scheduleNextNotification(goalId);
    }
}
