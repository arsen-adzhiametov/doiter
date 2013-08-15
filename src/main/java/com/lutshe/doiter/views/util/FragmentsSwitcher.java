package com.lutshe.doiter.views.util;

import android.app.Activity;
import android.app.Fragment;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.lutshe.doiter.R;

/**
 * Created by Artur
 */
@EBean(scope = Scope.Singleton)
public class FragmentsSwitcher {

    private Activity activity;

    public void show(int id, Fragment fragment) {
        activity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout)
                .replace(id, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
