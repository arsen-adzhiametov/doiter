//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.lutshe.doiter.views.usergoals.list;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.lutshe.doiter.views.util.FragmentsSwitcher_;

public final class UserGoalSelectedListener_
    extends UserGoalSelectedListener
{

    private Context context_;

    private UserGoalSelectedListener_(Context context) {
        context_ = context;
        init_();
    }

    public void afterSetContentView_() {
        if (!(context_ instanceof Activity)) {
            return ;
        }
        ((FragmentsSwitcher_) fragmentsSwitcher).afterSetContentView_();
        ((UserGoalsListAdapter_) userGoalsListAdapter).afterSetContentView_();
    }

    /**
     * You should check that context is an activity before calling this method
     * 
     */
    public View findViewById(int id) {
        Activity activity_ = ((Activity) context_);
        return activity_.findViewById(id);
    }

    @SuppressWarnings("all")
    private void init_() {
        if (context_ instanceof Activity) {
            Activity activity = ((Activity) context_);
        }
        fragmentsSwitcher = FragmentsSwitcher_.getInstance_(context_);
        userGoalsListAdapter = UserGoalsListAdapter_.getInstance_(context_);
    }

    public static UserGoalSelectedListener_ getInstance_(Context context) {
        return new UserGoalSelectedListener_(context);
    }

    public void rebind(Context context) {
        context_ = context;
        init_();
    }

}
