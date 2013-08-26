package com.lutshe.doiter.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.lutshe.doiter.MainActivity_;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;

import java.util.Random;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class NotificationFactory {

    @RootContext
    Context context;

    @Bean
    GoalsDao goalsDao;

    @Bean
    MessagesDao messagesDao;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    private static final Random random = new Random();

    public Notification createNotification(int quantity) {
        Bitmap icon = getRandomActualImage();
        String text = getRandomActualMessage();
        String title = getTitleText(quantity);

        Intent notificationIntent = new Intent(context, MainActivity_.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(icon)
                        .setAutoCancel(true)
                        .setTicker(text)
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title);

        return nb.build();
    }

    private String getTitleText(int quantity){
        String template = "You have a " + quantity + " new message";
        if (quantity == 1) return template;
        else return template+"s";
    }

    private Goal getRandomUserGoal(){
        Goal[] goals = goalsDao.getAllUserGoals();
        int i = random.nextInt(goals.length);
        return goals[i];
    }

    private String getRandomActualMessage(){
        Goal goal = getRandomUserGoal();
        return messagesDao.getMessage(goal.getId(), goal.getLastMessageIndex()).getText();
    }

    private Bitmap getRandomActualImage(){
        Goal goal = getRandomUserGoal();
        return imagesProvider.getImage(goal.getId());
    }

}
