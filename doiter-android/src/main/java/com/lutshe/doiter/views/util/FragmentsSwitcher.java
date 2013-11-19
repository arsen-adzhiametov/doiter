package com.lutshe.doiter.views.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.ActivityLifecycleListener;
import com.lutshe.doiter.views.UpdatableView;

/**
 * Created by Artur
 */
@EBean(scope = Scope.Singleton)
public class FragmentsSwitcher {

    private Activity activity;
    private int containerId = R.id.fragment_container;
    private Fragment currentFragment;

    private enum FragmentClass {
        GoalsListFragment_,
        GoalPreviewFragment_,
        UserGoalsListFragment_,
        UserGoalDetailFragment_,
        GoalsMapFragment_
    }

    public void show(Fragment fragment, boolean addToBackStack) {
        FragmentClass fragmentClass = FragmentClass.valueOf(fragment.getClass().getSimpleName());
        switch (fragmentClass) {
            case GoalsListFragment_:
            case GoalPreviewFragment_:
            case UserGoalDetailFragment_:
                doTransaction(fragment, addToBackStack);
                return;
            case UserGoalsListFragment_:
            case GoalsMapFragment_:
                activity.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                doTransaction(fragment, addToBackStack);
                return;
        }

    }

    private void doTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = activity.getFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        currentFragment = fragment;
        transaction.commit();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public Activity getActivity() {
        return activity;
    }

    public void updateCurrentFragment() {
        if (currentFragment instanceof UpdatableView) {
            ((UpdatableView) currentFragment).update();
        }
    }

    public void onResume() {
        if (currentFragment instanceof ActivityLifecycleListener) {
           ((ActivityLifecycleListener) currentFragment).resume();
        }
    }

    public void onPause() {
        if (currentFragment instanceof ActivityLifecycleListener) {
            ((ActivityLifecycleListener) currentFragment).pause();
        }
    }
}
