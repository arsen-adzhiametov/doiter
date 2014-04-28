package com.lutshe.doiter;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;
import com.lutshe.doiter.data.database.InitialDataSetup;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.preloaders.UpdatesAlarmListener;
import com.lutshe.doiter.views.goals.map.GoalsMapFragment;
import com.lutshe.doiter.views.goals.map.GoalsMapFragment_;
import com.lutshe.doiter.views.slidingtoolbar.SlidingToolbar;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.androidannotations.annotations.*;

import static com.lutshe.doiter.AchievementTimeConstants.OPEN_USER_GOALS_INTENT_ACTION;

@WindowFeature({ Window.FEATURE_NO_TITLE})
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById(R.id.sliding_layout)
    SlidingToolbar topMenuSlidingDrawer;

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

        fragmentsSwitcher.show(GoalsMapFragment_.builder().build());
    }

    private boolean isFirstLaunch() {
        return goalsDao.getAllGoalsCount() == 0;
    }

    @Trace
    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (OPEN_USER_GOALS_INTENT_ACTION.equals(action)){
            fragmentsSwitcher.show(UserGoalDetailFragment_.builder().build());
        }
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
        fragmentsSwitcher.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        fragmentsSwitcher.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        topMenuSlidingDrawer.setVisibility(View.VISIBLE);
        if (fragmentsSwitcher.getCurrentFragment() instanceof GoalsMapFragment){
            super.onBackPressed();
        }else{
            fragmentsSwitcher.show(GoalsMapFragment_.builder().build());
        }
    }
}
