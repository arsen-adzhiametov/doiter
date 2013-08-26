package com.lutshe.doiter.views.goals.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.views.timer.FinalCountdown;

/**
 * Created by Artur
 */
@EViewGroup(R.layout.goal_list_item)
public class GoalItemView extends LinearLayout {

    @ViewById(R.id.goalName)
    TextView goalName;

    @ViewById(R.id.goalCover)
    ImageView goalCover;

    @ViewById(R.id.textTimeCountdown)
    TextView timerView;

    public GoalItemView(Context context) {
        super(context);
    }

    public GoalItemView bind(Goal goal, Bitmap bitmap) {
        goalName.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        if (goal.getStatus() != Goal.Status.OTHER)
        new FinalCountdown(goal.getEndTime(), timerView).start();
        return this;
    }
}
