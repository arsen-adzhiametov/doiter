package com.lutshe.doiter.views.util;

import android.app.Activity;
import android.app.Fragment;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

/**
 * Created by Artur
 */
@EBean(scope = Scope.Singleton)
public class FragmentsSwitcher {

    private Activity activity;

    public void show(int id, Fragment fragment) {
        activity.getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(id, fragment)
                .commit();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
