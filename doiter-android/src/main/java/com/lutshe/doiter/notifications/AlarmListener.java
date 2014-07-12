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
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.Trace;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.List;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EReceiver
public class AlarmListener extends BroadcastReceiver {

    @SystemService NotificationManager notificationManager;
    @SystemService ActivityManager activityManager;
    @Bean NotificationFactory notificationFactory;
    @Bean MessagesDao messagesDao;
    @Bean GoalsDao goalsDao;

    @Override
    @Trace
    public void onReceive(Context context, Intent intent) {
        int deliveredMessagesCount = deliverMessages();
        if (deliveredMessagesCount > 0) {
            sendNotifications(context, deliveredMessagesCount);
        }
        Log.d("lutshe.alarm", "alarm fired. Sending " + deliveredMessagesCount + " notifications to user");
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
        int messagesSent = 0;
        Goal[] userGoals = goalsDao.getActiveUserGoals();
        if (userGoals == null) return messagesSent;
        for (Goal goal : userGoals) {
            messagesSent += ensureMessagesDeliveredOrDeliver(goal);
        }
        return messagesSent;
    }

    private int ensureMessagesDeliveredOrDeliver(Goal goal) {
        int messagesSent = 0;
        DateTime goalEndTime = getGoalEndTime(goal);
        DateTime goalStartTime = getGoalStartTime(goal);
        Days goalDistance = Days.daysBetween(goalStartTime, goalEndTime);

        DateTime nextMessageTime = goalStartTime.plusDays(getDaysCountWithDeliveredMessages(goal.getId()));
        while (nextMessageTime.isBeforeNow() &&
                getDaysCountWithDeliveredMessages(goal.getId()) < goalDistance.getDays()) {
            deliverNextMessage(goal.getId());
            messagesSent++;
            nextMessageTime = nextMessageTime.plusDays(1);
        }
        if (goalEndTime.isBeforeNow()) {
            deliverLastMessage(goal.getId());
            messagesSent++;
        }
        return messagesSent;
    }

    private DateTime getGoalEndTime(Goal goal) {
        return new DateTime(goal.getEndTime()).withTimeAtStartOfDay();
    }

    private DateTime getGoalStartTime(Goal goal) {
        Message firstDeliveredMessage = messagesDao.getMessage(goal.getId(), Message.Type.FIRST);
        return new DateTime(firstDeliveredMessage.getDeliveryTime()).withTimeAtStartOfDay();
    }

    private int getDaysCountWithDeliveredMessages(Long goalId) {
        Goal goal = goalsDao.getGoal(goalId);
        long lastMessageIndex = goal.getLastMessageIndex();
        return (int)lastMessageIndex + 1;
    }

    private void deliverNextMessage(Long goalId) {
        Goal goal = goalsDao.getGoal(goalId);
        long nextMessageIndex = goal.getLastMessageIndex() + 1;
        Message message = messagesDao.getMessage(goal.getId(), nextMessageIndex);
        if (message == null) {
            deliverLastMessage(goal.getId());
            return;
        }
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalLastMessage(goal.getId(), nextMessageIndex);
    }

    private void deliverLastMessage(Long goalId) {
        Message message = messagesDao.getMessage(goalId, Message.Type.LAST);
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalStatus(goalId, Goal.Status.INACTIVE);
    }

    private void sendNotification(int quantity) {
        Log.d("AL", "sending notifications");
        Notification notification = notificationFactory.createNotification(quantity);
        int notificationId = 0;
        notificationManager.notify(notificationId, notification);
    }
}
