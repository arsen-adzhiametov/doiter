package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;
import com.lutshe.doiter.views.timer.FinalCountdown;
import com.lutshe.doiter.views.usergoals.details.messages.MessagesListAdapter;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment {

    @ViewById(R.id.goalCoverDetail)
    ImageView goalCover;

    @ViewById(R.id.goalNameDetail)
    TextView goalName;

    @ViewById(R.id.userGoalMessagesList)
    ListView userGoalMessagesList;

    @ViewById(R.id.textTimeCountdown)
    TextView timerView;

    @Bean
    MessagesListAdapter messagesListAdapter;

    @Bean
    GoalsDao goalsDao;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        messagesListAdapter.initAdapter(goalId);
        userGoalMessagesList.setAdapter(messagesListAdapter);

        Goal goal = goalsDao.getGoal(goalId);
        goalName.setText(goal.getName());

        Bitmap bitmap = imagesProvider.getImage(goalId);
        goalCover.setImageBitmap(bitmap);

        new FinalCountdown(goal.getEndTime(), timerView).start();
    }
}
