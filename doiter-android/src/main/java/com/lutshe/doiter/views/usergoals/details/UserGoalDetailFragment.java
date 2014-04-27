package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.common.OurFont;
import com.lutshe.doiter.views.usergoals.details.messages.UserGoalMessagesListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import com.lutshe.doiter.views.util.StringUtils;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment implements UpdatableView {

    public static final long MILLISECONDS_IN_DAY = 1000L * 60 * 60 * 24;

    @ViewById(R.id.remaining)TextView remainingTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.goal_name)TextView goalNameTextView;
    @ViewById(R.id.more_tips_button_text)TextView backToAllButtonTextView;
    @ViewById(R.id.goal_cover)ImageView goalCover;
    @ViewById(R.id.message_number)TextView messageNumberTextView;
    @ViewById(R.id.web_view_content)WebView messageTextWebView;
    @ViewById(R.id.next_arrow_button)ImageView nextButton;
    @ViewById(R.id.prev_arrow_button)ImageView previousButton;

    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;
    @Bean(ImagesProviderImpl.class)ImagesProvider imagesProvider;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean HtmlCodePreparer htmlCodePreparer;
    @Bean OurFont font;

    @FragmentArg Long goalId;

    private Goal[] userGoals;
    private int currentGoalIndex;

    @AfterViews
    public void bindData() {
        Goal goal = goalsDao.getGoal(goalId);
        calculateCurrentGoalIndex();
        showGoalCover(goal);
        showTextViews(goal);
        loadCurrentMessageTextWebView(goal);
        setTypefaceToTextViews();
        disableNavigation();
    }

    private void showTextViews(Goal goal) {
        goalNameTextView.setText(goal.getName());
        int daysRemaining = getDaysRemaining(goal);
        daysQuantityTextView.setText(' '+String.valueOf(daysRemaining)+' ');
        daysTextTextView.setText(StringUtils.getDayOrDaysString(daysRemaining));
    }

    private void showGoalCover(Goal goal) {
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        goalCover.setImageBitmap(bitmap);
    }

    private void calculateCurrentGoalIndex() {
        userGoals = goalsDao.getAllUserGoals();
        for(int i = 0; i<userGoals.length; i++){
            if(userGoals[i].getId().equals(goalId)){
                currentGoalIndex = i;
            }
        }
    }

    private void disableNavigation() {
        if (userGoals.length == 1){
            nextButton.setVisibility(View.INVISIBLE);
            previousButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void update() {}

    private void setTypefaceToTextViews() {
        Typeface typeface = font.get();
        remainingTextView.setTypeface(typeface);
        daysQuantityTextView.setTypeface(typeface);
        daysTextTextView.setTypeface(typeface);
        goalNameTextView.setTypeface(typeface);
        backToAllButtonTextView.setTypeface(typeface);
        messageNumberTextView.setTypeface(typeface);
    }

    private int getDaysRemaining(Goal goal) {
        long timeDiff = goal.getEndTime() - DateTime.now().getMillis();
        return timeDiff > 0 ? (int)(timeDiff / MILLISECONDS_IN_DAY) : 0;
    }

    @Click(R.id.more_tips_button)
    void showAllTips(){
        fragmentsSwitcher.show(UserGoalMessagesListFragment_.builder().goalId(goalId).build());
    }

    @Click(R.id.prev_arrow_button)
    void showPreviousGoal(){
        showGoal(getPreviousUserGoalId());
    }

    @Click(R.id.next_arrow_button)
    void showNextGoal(){
        showGoal(getNextUserGoalId());
    }

    private void showGoal(long goalId){
        Fragment detailFragment = UserGoalDetailFragment_.builder().goalId(goalId).build();
        fragmentsSwitcher.show(detailFragment);
    }

    private long getNextUserGoalId(){
        int nextIndex = currentGoalIndex+1;
        if (nextIndex > userGoals.length-1){
            nextIndex = 0;
        }
        return userGoals[nextIndex].getId();
    }

    private long getPreviousUserGoalId(){
        int prevIndex = currentGoalIndex - 1;
        if (prevIndex < 0){
            prevIndex = userGoals.length - 1;
        }
        return userGoals[prevIndex].getId();
    }

    private void loadCurrentMessageTextWebView(Goal goal) {
        Message currentMessage = messagesDao.getMessage(goal.getId(), goal.getLastMessageIndex());
        String htmlCode = htmlCodePreparer.getHtmlCode(currentMessage.getText());
        messageTextWebView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
        messageTextWebView.setBackgroundColor(Color.TRANSPARENT);
        messageTextWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

}
