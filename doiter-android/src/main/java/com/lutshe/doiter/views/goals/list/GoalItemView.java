package com.lutshe.doiter.views.goals.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.model.Goal;
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

    @ViewById(R.id.text_time_countdown)
    TextView timerView;

    public GoalItemView(Context context) {
        super(context);
    }

    public GoalItemView bind(Goal goal, Bitmap bitmap) {
        goalName.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        if (goal.getStatus() != Goal.Status.OTHER)
        FinalCountdown.getTimer(goal, timerView).start();
        return this;
    }
}
