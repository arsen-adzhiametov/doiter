package com.lutshe.doiter.notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.Trace;
import com.lutshe.doiter.MainActivity_;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EReceiver
public class AlarmListener extends BroadcastReceiver {

    @SystemService
    NotificationManager notificationManager;
    @SystemService
    ActivityManager activityManager;

    @Bean
    NotificationFactory notificationFactory;

    @Bean
    MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;

    @Bean
    MessagesDao messagesDao;

    @Bean
    GoalsDao goalsDao;

    @Override
    @Trace
    public void onReceive(Context context, Intent intent) {
        try {
            int quantity = deliverMessages();
            if (quantity > 0) {
                sendNotifications(context, quantity);
            }
        } finally {
            scheduleNext();
        }
    }

    private void sendNotifications(Context context, int quantity) {
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

    private int deliverMessages() {
        Goal[] userGoals = goalsDao.getActiveUserGoals();
        if (userGoals == null) return 0;
        for (Goal goal : userGoals) {
            Log.d("notification alarm", "delivering messages for goal " + goal);
            DateTime goalEndTime = new DateTime(goal.getEndTime());
            if (isToday(goalEndTime)) {
                deliverLastMessage(goal);
            } else {
                deliverOtherMessage(goal);
            }
        }
        return userGoals.length;
    }

    private void deliverOtherMessage(Goal goal) {
        long nextMessageIndex = goal.getLastMessageIndex() + 1;
        Message message = messagesDao.getMessage(goal.getId(), nextMessageIndex);
        if (message == null) {
            deliverLastMessage(goal);
            return;
        }
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalLastMessage(goal.getId(), nextMessageIndex);
    }

    private void deliverLastMessage(Goal goal) {
        Message message = messagesDao.getMessage(goal.getId(), Message.Type.LAST);
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalStatus(goal.getId(), Goal.Status.INACTIVE);
    }

    private boolean isToday(DateTime goalEndTime) {
        return goalEndTime.getDayOfYear() == DateTime.now().getDayOfYear();
    }

    private void sendNotification(int quantity) {
        Log.d("AL", "sending notifications");
        Notification notification = notificationFactory.createNotification(quantity);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }

    private void scheduleNext() {
        messagesUpdateAlarmScheduler.scheduleNextAlarm();
    }
}
