package com.lutshe.doiter.views.goals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.notifications.NotificationScheduler;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.DatabaseHelper;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

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

    @ViewById(R.id.editEndTime)
    EditText editEndTime;

    @Bean(GoalsProviderStub.class)
    GoalsProvider goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    @Bean
    DatabaseHelper databaseHelper;

    @Bean
    FragmentsSwitcher fragmentsSwitcher;

    @Bean
    NotificationScheduler notificationScheduler;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        Goal goal = goalsProvider.getGoalById(goalId);
        goalName.setText(goal.getName());

        Bitmap bitmap = imagesProvider.getImage(goalId);
        goalCover.setImageBitmap(bitmap);
    }

    @Click(R.id.addGoalButton)
    void addGoal(){
        Long endTime = Long.valueOf(editEndTime.getText().toString());
        databaseHelper.addGoal(goalId, endTime);
        notificationScheduler.scheduleNextNotification(goalId);
        fragmentsSwitcher.show(R.id.fragment_container, UserGoalsListFragment_.builder().build());
    }
}
