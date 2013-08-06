package com.lutshe.doiter.views.goals;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.model.Goal;

/**
 * Created by Artur
 */
@EViewGroup(R.layout.goal_list_item)
public class GoalItemView extends LinearLayout {

    @ViewById
    TextView goalName;

    @ViewById
    ImageView goalCover;

    public GoalItemView(Context context) {
        super(context);
    }

    public GoalItemView bind(Goal goal, Bitmap bitmap) {
        goalName.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        return this;
    }
}
