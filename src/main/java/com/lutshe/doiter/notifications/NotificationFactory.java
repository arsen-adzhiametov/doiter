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
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class NotificationFactory {

    @RootContext
    Context context;

    @Bean(GoalsProviderStub.class)
    GoalsProvider goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    public Notification createNotification(int quantity) {
        Long goalId = 1L; //TODO
        Bitmap icon = imagesProvider.getImage(goalId);
        String text = "Blah - Blah - Blah - Blah";  //TODO
        String title = "You have a " + quantity + " new messages";   //TODO

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

}
