package com.lutshe.doiter.views.goals.preview;

import android.app.Fragment;
import android.view.View;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.notifications.MessagesUpdateAlarmScheduler;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

import static com.lutshe.doiter.dto.MessageDTO.Type;

/**
 * Created by Arsen Adzhiametov on 6/31/13.
 */
@EFragment(R.layout.goal_preview_fragment)
public class GoalPreviewFragment extends Fragment {

    @ViewById(R.id.goal_preview_fragment_view) GoalPreviewLayout goalPreviewLayout;

    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        Goal goal = goalsDao.getGoal(goalId);
        Message firstMessage = messagesDao.getMessage(goalId, Type.FIRST);
        goalPreviewLayout.showGoalName(goal.getName());
        goalPreviewLayout.showGoalDescription(firstMessage.getText());
        goalPreviewLayout.setSeekBarMaximum(messagesDao.getGoalMessagesCount(goalId));
    }

    @Click(R.id.add_goal_button)
    void addToUserGoals() {
        addFirstMessage();
        activateGoal();
        scheduleMessageSending();
        showGoal();
        showTopMenu();
        sendAnalyticsEvent();
    }

    private void sendAnalyticsEvent() {
        EasyTracker tracker = EasyTracker.getInstance(getActivity());
        String goalName = goalPreviewLayout.goalNameTextView.getText().toString();
        tracker.send(MapBuilder.createEvent("goal_selection","goal_selected", goalName, 1L).build());
    }

    private void showTopMenu() {
        goalPreviewLayout.setTopMenuVisibility(View.VISIBLE);
    }

    private void showGoal() {
        fragmentsSwitcher.show(UserGoalDetailFragment_.builder().goalId(goalId).build());
    }

    private void scheduleMessageSending() {
        messagesUpdateAlarmScheduler.scheduleAlarmIfNotSet();
    }

    private void activateGoal() {
        int days = goalPreviewLayout.getSeekBarCurrentValue();
        Long endTime = DateTime.now().plusDays(days).getMillis();
        goalsDao.updateGoalEndTime(goalId, endTime);
        goalsDao.updateGoalStatus(goalId, Goal.Status.ACTIVE);
    }

    private void addFirstMessage() {
        Message message = messagesDao.getMessage(goalId, Type.FIRST);
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalLastMessage(goalId, message.getOrderIndex());
    }

}
