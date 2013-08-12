package com.lutshe.doiter.views.goals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.DatabaseHelper;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.UserGoal;
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.goal_detail_fragment)
public class GoalDetailFragment extends Fragment {

    public static final String GOAL_ID = "goalId";

    @ViewById(R.id.goalCoverDetail)
    ImageView goalCover;

    @ViewById(R.id.goalNameDetail)
    TextView goalName;

    @ViewById(R.id.goalEndTime)
    TextView goalEndTime;

    @Bean(GoalsProviderStub.class)
    GoalsProvider goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    @Bean
    DatabaseHelper databaseHelper;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        Goal goal = goalsProvider.getGoalById(goalId);
        goalName.setText(goal.getName());

        Bitmap bitmap = imagesProvider.getImage(goalId);
        goalCover.setImageBitmap(bitmap);

        UserGoal userGoal = databaseHelper.getUserGoal(goalId);
        goalEndTime.setText(userGoal.getEndTime().toString());
    }
}
