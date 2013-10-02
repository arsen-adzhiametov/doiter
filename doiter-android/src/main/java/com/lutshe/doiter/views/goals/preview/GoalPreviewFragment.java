package com.lutshe.doiter.views.goals.preview;

import android.app.Fragment;
import android.graphics.Typeface;
import android.widget.SeekBar;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.notifications.MessagesUpdateAlarmScheduler;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.goal_preview_fragment)
public class GoalPreviewFragment extends Fragment {

//    @ViewById(R.id.goalCoverDetail)
//    ImageView goalCover;

    @ViewById(R.id.goalNameDetail)
    TextView goalName;

    @ViewById(R.id.daysSetting)
    TextView days;

    @ViewById(R.id.iWillDoItIn)
    TextView iWillDoItInTextView;

    @ViewById(R.id.seekbar)
    SeekBar seekBar;

    @Bean SeekBarChangeListener seekBarChangeListener;
    @Bean(ImagesProviderImpl.class) ImagesProvider imagesProvider;
    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        Goal goal = goalsDao.getGoal(goalId);
        goalName.setText(goal.getName());

//        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
//        goalCover.setImageBitmap(bitmap);

        setTypefaceToTextViews();
    }

    @Click(R.id.addGoalButton)
    void addToUserGoals() {
        addFirstMessage();
        activateGoal();
        scheduleNextAlarm();
        showGoal();
    }

    private void showGoal() {
        fragmentsSwitcher.show(UserGoalsListFragment_.builder().build(), false);
    }

    private void scheduleNextAlarm() {
        messagesUpdateAlarmScheduler.scheduleNextAlarm();
    }

    private void activateGoal() {
        int days = seekBar.getProgress();
        Long endTime = DateTime.now().plusDays(days).getMillis();
        goalsDao.updateGoalEndTime(goalId, endTime);
        goalsDao.updateGoalStatus(goalId, Goal.Status.ACTIVE);
    }

    private void addFirstMessage() {
        Message message = messagesDao.getMessage(goalId, Message.Type.FIRST);
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalLastMessage(goalId, message.getOrderIndex());
    }

    private void setTypefaceToTextViews() {
        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        goalName.setTypeface(typeface);
        days.setTypeface(typeface);
        iWillDoItInTextView.setTypeface(typeface);
    }

}
