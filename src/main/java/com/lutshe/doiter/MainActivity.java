package com.lutshe.doiter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

@NoTitle
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @AfterViews
    void setFragment() {
        GoalsListFragment newFragment = new GoalsListFragment_();
        addFragment(newFragment);
    }

    public void addFragment(Fragment newFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }
}
