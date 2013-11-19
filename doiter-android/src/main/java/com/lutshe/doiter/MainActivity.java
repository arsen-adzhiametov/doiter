package com.lutshe.doiter;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.Trace;
import com.lutshe.doiter.data.database.InitialDataSetup;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.preloaders.UpdatesAlarmListener;
import com.lutshe.doiter.views.goals.map.GoalsMapFragment_;
import com.lutshe.doiter.views.timer.FinalCountdown;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.newrelic.agent.android.NewRelic;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean GoalsDao goalsDao;
    @Bean InitialDataSetup testDataSetup;

    @SystemService AlarmManager alarmManager;

    @AfterViews
    void initViews() {
        Crashlytics.start(this);
        NewRelic.withApplicationToken("AA8a51d35143bc258445a7cb114119ad09312499f1").start(this.getApplication());

        fragmentsSwitcher.setActivity(this);

        if (isFirstLaunch()) {
            testDataSetup.setup();
            UpdatesAlarmListener.scheduleUpdates(getApplicationContext());
        }

        if (goalsDao.getUserGoalsCount() == 0) {
            fragmentsSwitcher.show(GoalsMapFragment_.builder().build(), false);
        } else {
            fragmentsSwitcher.show(UserGoalsListFragment_.builder().build(), false);
        }
    }

    private boolean isFirstLaunch() {
        return goalsDao.getAllGoalsCount() == 0;
    }

    @Trace
    @Override
    protected void onNewIntent(Intent intent) {
        fragmentsSwitcher.updateCurrentFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onPause() {
        FinalCountdown.invalidateTimers();
        fragmentsSwitcher.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        fragmentsSwitcher.onResume();
        super.onResume();
    }
}
