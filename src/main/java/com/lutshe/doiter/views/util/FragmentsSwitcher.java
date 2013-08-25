package com.lutshe.doiter.views.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.lutshe.doiter.R;

/**
 * Created by Artur
 */
@EBean(scope = Scope.Singleton)
public class FragmentsSwitcher {

    private Activity activity;
    private int containerId = R.id.fragment_container;

    private enum FragmentClass {
        GoalsListFragment_,
        GoalPreviewFragment_,
        UserGoalsListFragment_,
        UserGoalDetailFragment_
    }

    public void show(Fragment fragment, boolean addToBackStack) {
        FragmentClass fragmentClass = FragmentClass.valueOf(fragment.getClass().getSimpleName());
        switch (fragmentClass) {
            case GoalsListFragment_:
                doTransaction(fragment, addToBackStack);
                return;
            case GoalPreviewFragment_:
                doTransaction(fragment, addToBackStack);
                return;
            case UserGoalDetailFragment_:
                doTransaction(fragment, addToBackStack);
                return;
            case UserGoalsListFragment_:
                activity.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                doTransaction(fragment, addToBackStack);
                return;
        }

    }

    private void doTransaction(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            activity.getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout)
                    .replace(containerId, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            activity.getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout)
                    .replace(containerId, fragment)
                    .commit();
        }

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
