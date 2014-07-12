package com.lutshe.doiter.notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.lutshe.doiter.MainActivity_;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.Trace;

import java.util.List;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EReceiver
public class MessagesUpdateAlarmListener extends BroadcastReceiver {

    @SystemService NotificationManager notificationManager;
    @SystemService ActivityManager activityManager;
    @Bean NotificationFactory notificationFactory;
    @Bean MessagesUpdater messagesUpdater;

    @Override
    @Trace
    public void onReceive(Context context, Intent intent) {
        int deliveredMessagesCount = messagesUpdater.deliverMessages();
        if (deliveredMessagesCount > 0) {
            sendUpdate(context, deliveredMessagesCount);
        }
        Log.d("lutshe.alarm", "alarm fired. Sending " + deliveredMessagesCount + " notifications to user");
    }

    private void sendUpdate(Context context, int quantity) {
        if (isActivityRunning()) {
            notifyActivity(context);
        } else {
            sendNotification(quantity);
        }
    }

    private boolean isActivityRunning() {
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        ComponentName runningActivity = tasks.get(0).topActivity;
        return runningActivity.getPackageName().startsWith("com.lutshe.doiter");
    }

    private void notifyActivity(Context context) {
        Log.d("AL", "notifying activity about new messages");
        Intent intent = new Intent(context, MainActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void sendNotification(int quantity) {
        Log.d("AL", "sending notifications");
        Notification notification = notificationFactory.createNotification(quantity);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }
}
