package com.lutshe.doiter.views.goals.preview;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;
import com.lutshe.doiter.notifications.NotificationScheduler;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.goal_preview_fragment)
public class GoalPreviewFragment extends Fragment {

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

    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;

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
    void addToUserGoals() {
        setGoalEndTime();
        addFirstMessage();
        scheduleNextMessage();
        showGoal();
    }

    private void showGoal() {
        fragmentsSwitcher.show(R.id.fragment_container, UserGoalsListFragment_.builder().build());
    }

    private void scheduleNextMessage() {
        notificationScheduler.scheduleNextNotification(goalId);
    }

    private void setGoalEndTime() {
        Long endTime = Long.valueOf(editEndTime.getText().toString());
        goalsDao.updateGoalEndTime(goalId, endTime);
    }

    private void addFirstMessage() {
        Message message = messagesDao.getMessage(goalId, Message.Type.FIRST);
        messagesDao.addMessage(message);
    }
}
