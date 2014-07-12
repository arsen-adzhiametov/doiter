package com.lutshe.doiter;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;
import com.lutshe.doiter.data.database.InitialDataSetup;
import com.lutshe.doiter.data.database.dao.DatabaseHelper;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.preloaders.UpdatesAlarmListener;
import com.lutshe.doiter.views.goals.map.GoalsMapFragment;
import com.lutshe.doiter.views.goals.map.GoalsMapFragment_;
import com.lutshe.doiter.views.slidingtoolbar.SlidingToolbar;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.BooleanRes;

import static com.lutshe.doiter.AchievementTimeConstants.OPEN_USER_GOALS_INTENT_ACTION;

@WindowFeature({ Window.FEATURE_NO_TITLE})
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewById(R.id.sliding_toolbar)
    SlidingToolbar toolbar;

    @BooleanRes boolean copyDatabaseFromFile;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean GoalsDao goalsDao;
    @Bean InitialDataSetup testDataSetup;
    @Bean DatabaseHelper databaseHelper;

    @SystemService AlarmManager alarmManager;

    @AfterViews
    void initViews() {
        Crashlytics.start(this);
        fragmentsSwitcher.init(this);

        if (isFirstLaunch()) {
            if (copyDatabaseFromFile) {
                databaseHelper.copyDatabaseFromFile();
            } else {
                testDataSetup.setup();
            }
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
    public boolean onTouchEvent(MotionEvent event) {
        // hide top toolbar on any click.
        // this will not work if any touch listener consumes touch event (returns true)
        toolbar.hide();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        toolbar.setVisibility(View.VISIBLE);
        if (fragmentsSwitcher.getCurrentFragment() instanceof GoalsMapFragment){
            super.onBackPressed();
        }else{
            fragmentsSwitcher.show(GoalsMapFragment_.builder().build());
        }
    }
}
