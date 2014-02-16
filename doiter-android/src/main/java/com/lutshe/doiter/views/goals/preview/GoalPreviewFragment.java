package com.lutshe.doiter.views.goals.preview;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.notifications.MessagesUpdateAlarmScheduler;
import com.lutshe.doiter.views.BackStackable;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import com.lutshe.doiter.views.util.StringUtils;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 6/31/13.
 */
@EFragment(R.layout.goal_preview_fragment)
public class GoalPreviewFragment extends Fragment implements BackStackable{

    @ViewById(R.id.goal_name)TextView goalNameTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.i_will_do_it_in_text_view)TextView iWillDoItInTextView;
    @ViewById(R.id.web_view_content)WebView goalDescriptionView;
    @ViewById(R.id.add_goal_text)TextView addGoalTextView;
    @ViewById(R.id.seekbar)SeekBar seekBar;
    @ViewById(R.id.message_number)TextView messageNumberTextView;

    @Bean(ImagesProviderImpl.class) ImagesProvider imagesProvider;
    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean MessagesUpdateAlarmScheduler messagesUpdateAlarmScheduler;
//    @Bean ViewPaddingAdapter viewPaddingAdapter;
    @Bean HtmlCodePreparer htmlCodePreparer;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        setTopMenuVisibility(View.INVISIBLE);
        Goal goal = goalsDao.getGoal(goalId);
        goalNameTextView.setText(goal.getName());
        loadGoalDescriptionWebView(getResources().getString(R.string.goal_description));
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
    public void onProgressChanged(int progress) {
        String text = StringUtils.getDayOrDaysString(progress);
        daysQuantityTextView.setText(" " + progress + " ");
        daysTextTextView.setText(text);

    }

    private void showGoal() {
        fragmentsSwitcher.show(UserGoalDetailFragment_.builder().goalId(goalId).build(), false);
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
        View topMenuSlidingDrawer = getActivity().findViewById(R.id.top_menu_sliding_drawer);
        topMenuSlidingDrawer.setVisibility(visibility);
    }

    private void loadGoalDescriptionWebView(String goalDescription) {
        String htmlCode = htmlCodePreparer.getHtmlCode(goalDescription);
        goalDescriptionView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
        goalDescriptionView.setBackgroundColor(Color.TRANSPARENT);
        goalDescriptionView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    private void setTypefaceToTextViews() {
        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        goalNameTextView.setTypeface(typeface);
        daysQuantityTextView.setTypeface(typeface);
        iWillDoItInTextView.setTypeface(typeface);
        daysTextTextView.setTypeface(typeface);
        addGoalTextView.setTypeface(typeface);
//        messageNumberTextView.setTypeface(typeface);
    }

}
