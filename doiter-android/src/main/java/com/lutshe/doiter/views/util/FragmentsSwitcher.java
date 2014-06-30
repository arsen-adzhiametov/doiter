package com.lutshe.doiter.views.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.ActivityLifecycleListener;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.slidingtoolbar.SlidingToolbar;
import org.androidannotations.annotations.EBean;

/**
 * Created by Artur
 */
@EBean(scope = EBean.Scope.Singleton)
public class FragmentsSwitcher {

    private Activity activity;
    private Fragment currentFragment;
    private SlidingToolbar toolbar;

    public void show(Fragment fragment) {
        toolbar.hide();

        FragmentTransaction transaction = activity.getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);
        currentFragment = fragment;
        transaction.commit();
    }

    public void init(Activity activity) {
        this.activity = activity;
        this.toolbar = (SlidingToolbar) activity.findViewById(R.id.sliding_toolbar);
    }
    public Activity getActivity() {
        return activity;
    }
    public Fragment getCurrentFragment() {
        return currentFragment;
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
