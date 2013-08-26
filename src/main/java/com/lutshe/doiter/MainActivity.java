package com.lutshe.doiter;

import android.app.Activity;
import android.app.AlarmManager;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.SystemService;
import com.lutshe.doiter.data.database.TestDataSetup;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.preloaders.AppListener;
import com.lutshe.doiter.views.goals.list.GoalsListFragment_;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean GoalsDao goalsDao;
    @Bean TestDataSetup testDataSetup;


    @SystemService AlarmManager alarmManager;

    @AfterViews
    void initViews() {
        fragmentsSwitcher.setActivity(this);

        if (isFirstLaunch()) {
            testDataSetup.setup();
            WakefulIntentService.scheduleAlarms(new AppListener(), this, true);
        }

        if (goalsDao.getActiveUserGoalsCount() == 0) {
            fragmentsSwitcher.show(GoalsListFragment_.builder().build(), false);
        } else {
            fragmentsSwitcher.show(UserGoalsListFragment_.builder().build(), false);
        }
    }

    private boolean isFirstLaunch() {
        return goalsDao.getAllGoalsCount() == 0;
    }
}
