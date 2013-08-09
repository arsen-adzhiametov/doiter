package com.lutshe.doiter;

import android.app.Activity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.lutshe.doiter.views.goals.list.GoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Bean
    FragmentsSwitcher fragmentsSwitcher;

    @AfterViews
    public void initViews() {
        fragmentsSwitcher.setActivity(this);
        fragmentsSwitcher.show(R.id.fragment_container, GoalsListFragment_.builder().build());
    }
}
