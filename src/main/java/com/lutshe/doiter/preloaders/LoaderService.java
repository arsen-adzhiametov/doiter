package com.lutshe.doiter.preloaders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;

import org.joda.time.DateTime;

import retrofit.RetrofitError;

/**
 * Created by Arturro on 24.08.13.
 */
@EService
public class LoaderService extends WakefulIntentService {
    private static String TAG = LoaderService.class.getName();

    @Bean GoalsLoader goalsLoader;
    @Bean MessagesLoader messagesLoader;

    private static Loader[] loaders;

    @AfterInject
    void init() {
        loaders = new Loader[] {
            goalsLoader, messagesLoader
        };
    }

    public LoaderService() {
        super("Preloader Service");
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        Log.d(TAG ,"starting preloading service");

        if (!isNetworkAvailable()) {
            Log.d(TAG ,"no internet access - skipping this update");
            return;
        }

        for (Loader loader : loaders) {
            long lastCallTime = getLastCallTime(loader);
            Log.i(TAG, "last call time for " + loader.getClass().getName() + " is " + new DateTime(lastCallTime).toString());

            boolean shouldBeCalled = System.currentTimeMillis() >= lastCallTime + loader.getLoadingInterval();
            Log.i(TAG, shouldBeCalled ? "calling it" : "skipping it");
            if (shouldBeCalled) {
                callLoader(loader);
            }
        }
    }

    private void callLoader(Loader loader) {
        Log.i(TAG, "calling loader: " + loader.getClass());
        try {
            loader.load();
            updateLastCallTime(loader);
        }
        catch (RetrofitError re) {
            Log.w(TAG, "error calling service: " + re.getCause().getMessage());
        }
        catch (Throwable e) {
            Log.e(TAG, "failed to execute loader", e);
        }
    }

    private long getLastCallTime(Loader loader) {
        return getSharedPreferences("loaders", MODE_PRIVATE).getLong(loader.getClass() + "lastCall", 0);
    }

    private void updateLastCallTime(Loader loader) {
        getSharedPreferences("loaders", MODE_PRIVATE).edit().putLong(loader.getClass() + "lastCall", System.currentTimeMillis()).commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
