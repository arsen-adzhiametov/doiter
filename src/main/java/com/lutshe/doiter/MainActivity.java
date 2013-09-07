package com.lutshe.doiter;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.data.database.InitialDataSetup;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.preloaders.UpdatesAlarmListener;
import com.lutshe.doiter.views.goals.list.GoalsListFragment_;
import com.lutshe.doiter.views.timer.FinalCountdown;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.crashlytics.android.Crashlytics;

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
        fragmentsSwitcher.setActivity(this);

        if (isFirstLaunch()) {
            testDataSetup.setup();
            UpdatesAlarmListener.scheduleUpdates(getApplicationContext());
        }

        if (goalsDao.getUserGoalsCount() == 0) {
            fragmentsSwitcher.show(GoalsListFragment_.builder().build(), false);
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
    protected void onPause() {
        FinalCountdown.invalidateTimers();
        super.onPause();
    }
}
