package com.lutshe.doiter.preloaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.commonsware.cwac.wakeful.WakefulIntentService;
import org.joda.time.DateTime;

import static android.content.Context.MODE_PRIVATE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Created by Arsen Adzhiametov on 9/09/13.
 */
public class InternetConnectionListener extends BroadcastReceiver {

    private static final String TAG = InternetConnectionListener.class.getName();
    private static final int MAX_DAYS_PERIOD_WITHOUT_UPDATE = 5;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "Action: " + intent.getAction());
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info == null) return;

            boolean available = info.isAvailable();
            Log.i(TAG, "Network Type: " + info.getTypeName()
                    + ", subtype: " + info.getSubtypeName()
                    + ", available: " + available);

            if (available && (info.getType() == TYPE_WIFI || isLongPeriodWithoutUpdate())) {
                WakefulIntentService.sendWakefulWork(context, LoaderService_.class);
            }
        }
    }

    private boolean isLongPeriodWithoutUpdate() {
        DateTime lastSyncTime = new DateTime(getLastSyncTime());
        return lastSyncTime.plusDays(MAX_DAYS_PERIOD_WITHOUT_UPDATE).isBeforeNow();
    }

    private long getLastSyncTime() {
        long lastGoalSyncTime = context.getSharedPreferences("loaders", MODE_PRIVATE).getLong(MessagesLoader.class + "lastCall", 0);
        long lastMessageSyncTime = context.getSharedPreferences("loaders", MODE_PRIVATE).getLong(GoalsLoader.class + "lastCall", 0);
        return lastGoalSyncTime > lastMessageSyncTime ? lastMessageSyncTime : lastGoalSyncTime;
    }

}
