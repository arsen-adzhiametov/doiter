package com.lutshe.doiter.preloaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.commonsware.cwac.wakeful.WakefulIntentService;

/**
 * Created by Arsen Adzhiametov on 9/09/13.
 */
public class InternetConnectionListener extends BroadcastReceiver {

	private static final String TAG = InternetConnectionListener.class.getName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Action: " + intent.getAction());
		
    	if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
		    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		    
		    if (info == null) return;
		    
		    String typeName = info.getTypeName();
		    String subtypeName = info.getSubtypeName();
		    boolean available = info.isAvailable();
		    
		    Log.i(TAG, "Network Type: " + typeName
				+ ", subtype: " + subtypeName
				+ ", available: " + available);	
		    
		    if (available) {
                WakefulIntentService.sendWakefulWork(context, LoaderService_.class);
		    }
    	}
	}

}
