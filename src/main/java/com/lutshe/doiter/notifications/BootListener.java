package com.lutshe.doiter.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.lutshe.doiter.data.database.DatabaseHelper;
import com.lutshe.doiter.data.model.UserGoal;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EReceiver
public class BootListener extends BroadcastReceiver {

    @Bean
    DatabaseHelper databaseHelper;

    @Bean
    NotificationScheduler notificationScheduler;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("doiter_on_boot", "phone boot complete");
        UserGoal[] userGoal = databaseHelper.getAllUserGoals();
        for(int i = 0; i < userGoal.length; i++){
            notificationScheduler.scheduleNextNotification(userGoal[i].getGoalId());
        }
    }
}
