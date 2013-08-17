package com.lutshe.doiter;

import android.app.Activity;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.data.database.DatabaseHelper;
import com.lutshe.doiter.views.goals.list.GoalsListFragment_;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Bean
    FragmentsSwitcher fragmentsSwitcher;

    @Bean
    DatabaseHelper databaseHelper;

    @AfterViews
    public void initViews() {
        fragmentsSwitcher.setActivity(this);
        if (databaseHelper.getUserGoalsCount() == 0)
            fragmentsSwitcher.show(R.id.fragment_container, GoalsListFragment_.builder().build());
        else
            fragmentsSwitcher.show(R.id.fragment_container, UserGoalsListFragment_.builder().build());
    }
}
