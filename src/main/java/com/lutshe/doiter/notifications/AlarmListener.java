package com.lutshe.doiter.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.dao.MessagesDao;
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
    MessagesDao messagesDao;

    @Bean(MessagesProviderStub.class)
    MessagesProvider messagesProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        Long goalId = (Long) intent.getExtras().get("goalId");
        Long messageId = (Long) intent.getExtras().get("messageId");

        Message message = messagesProvider.getRandomMessage(goalId);
        messagesDao.updateMessageDeliveryTime(messageId);

        sendNotification(message);
        notificationScheduler.scheduleNextNotification(goalId);
    }

    private void sendNotification(Message message) {
        Notification notification = notificationFactory.createNotification(message);
        int notificationId = (int)message.getUserGoalId();
        notificationManager.notify(notificationId, notification);
    }
}
