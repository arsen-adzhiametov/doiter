package com.lutshe.doiter.views.goals.preview;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import org.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.notifications.MessagesUpdateAlarmScheduler;
import com.lutshe.doiter.views.BackStackable;
import com.lutshe.doiter.views.usergoals.list.UserGoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 6/31/13.
 */
@EFragment(R.layout.goal_preview_fragment)
public class GoalPreviewFragment extends Fragment implements BackStackable{

    @ViewById(R.id.goalNameDetail)TextView goalNameTextView;
    @ViewById(R.id.daysText)TextView daysTextTextView;
    @ViewById(R.id.daysQuantity)TextView daysQuantityTextView;
    @ViewById(R.id.i_will_do_it_in_text_view)TextView iWillDoItInTextView;
    @ViewById(R.id.goal_description)TextView goalDescriptionTextView;
    @ViewById(R.id.addGoalText)TextView addGoalTextView;
    @ViewById(R.id.seekbar)SeekBar seekBar;

    @Bean(ImagesProviderImpl.class) ImagesProvider imagesProvider;
    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;
    @Bean ViewPaddingAdapter viewPaddingAdapter;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        setTopMenuVisibility(View.INVISIBLE);
        Goal goal = goalsDao.getGoal(goalId);
        goalNameTextView.setText(goal.getName());
        setTypefaceToTextViews();
    }

    @Click(R.id.addGoalButton)
    void addToUserGoals() {
        addFirstMessage();
        activateGoal();
        scheduleNextAlarm();
        showGoal();
        setTopMenuVisibility(View.VISIBLE);

        EasyTracker tracker = EasyTracker.getInstance(getActivity());
        tracker.send(MapBuilder.createEvent("goal_selection", "goal_selected", goalNameTextView.getText().toString(), 1L).build());
    }

    @SeekBarProgressChange(R.id.seekbar)
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String text = getAppropriateString(progress);
        daysQuantityTextView.setText(" " + progress + " ");
        daysTextTextView.setText(text);

    }

    //to utils
    private String getAppropriateString(int progress) {
        String number = String.valueOf(progress);
        if ((number.endsWith("2") || number.endsWith("3") || number.endsWith("4")) && !number.startsWith("1")){
            return  "дня  "; //whitespace is necessary for layout constant width
        } else if (!String.valueOf(progress).endsWith("1") || progress == 11) {
            return  "дней";
        } else {
            return  "день";
        }
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

    private void setTopMenuVisibility(int visibility) {
        Activity activity = fragmentsSwitcher.getActivity();
        View topMenuSlidingDrawer = activity.findViewById(R.id.top_menu_sliding_drawer);
        topMenuSlidingDrawer.setVisibility(visibility);
    }

    private void setTypefaceToTextViews() {
        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        goalNameTextView.setTypeface(typeface);
        daysQuantityTextView.setTypeface(typeface);
        iWillDoItInTextView.setTypeface(typeface);
        daysTextTextView.setTypeface(typeface);
        addGoalTextView.setTypeface(typeface);
        goalDescriptionTextView.setTypeface(typeface);
    }

}
