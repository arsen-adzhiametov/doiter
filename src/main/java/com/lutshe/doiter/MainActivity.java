package com.lutshe.doiter;

import android.app.Activity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.lutshe.doiter.data.database.TestDataSetup;
import com.lutshe.doiter.data.database.dao.GoalsDao;
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

    @AfterViews
    void initViews() {
        fragmentsSwitcher.setActivity(this);

        if (isFirstLaunch()) {
            testDataSetup.setup();
        }

        if (goalsDao.getUserGoalsCount() == 0) {
            fragmentsSwitcher.show(R.id.fragment_container, GoalsListFragment_.builder().build());
        } else {
            fragmentsSwitcher.show(R.id.fragment_container, UserGoalsListFragment_.builder().build());
        }
    }

    private boolean isFirstLaunch() {
        return goalsDao.getAllGoalsCount() == 0;
    }
}
