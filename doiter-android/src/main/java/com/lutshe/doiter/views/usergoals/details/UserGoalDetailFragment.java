package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.common.OurFont;
import com.lutshe.doiter.views.usergoals.details.messages.UserGoalMessagesListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment implements UpdatableView {

    public static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    @ViewById(R.id.remaining)TextView remainingTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.goal_name)TextView goalNameTextView;
    @ViewById(R.id.more_tips_button_text)TextView backToAllButtonTextView;
    @ViewById(R.id.goal_cover)ImageView goalCover;
    @ViewById(R.id.message_number)TextView messageNumberTextView;
    @ViewById(R.id.web_view_content)WebView messageTextWebView;

    @Bean GoalsDao goalsDao;
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
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        goalNameTextView.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        daysQuantityTextView.setText(String.valueOf(getDaysRemaining(goal)));
        loadCurrentMessageTextWebView(getResources().getString(R.string.message_stub));
        setTypefaceToTextViews();
        userGoals = goalsDao.getAllUserGoals();
        for(int i = 0; i<userGoals.length; i++){
            if(userGoals[i].getId().equals(goalId)){
                currentGoalIndex = i;
            }
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

    private long getDaysRemaining(Goal goal) {
        long timeDiff = goal.getEndTime() - DateTime.now().getMillis();
        long daysRemaining = timeDiff/ MILLISECONDS_IN_DAY;
        return daysRemaining;
    }

    @Click(R.id.more_tips_button)
    void showAllTips(){
        fragmentsSwitcher.show(UserGoalMessagesListFragment_.builder().goalId(goalId).build());
    }

    @Click(R.id.prev_arrow_button)
    void showPreviousGoal(){
        showGoal(Direction.PREVIOUS);
    }

    @Click(R.id.next_arrow_button)
    void showNextGoal(){
         showGoal(Direction.NEXT);
    }

    private enum Direction {
        NEXT, PREVIOUS
    }

    private void showGoal(Direction direction){
        if(userGoals.length > 1) {
            long goalId = this.goalId;
            switch (direction) {
                case NEXT:
                    goalId = getNextUserGoalId();
                    break;
                case PREVIOUS:
                    goalId = getPreviousUserGoalId();
            }
            Fragment detailFragment = UserGoalDetailFragment_.builder().goalId(goalId).build();
            fragmentsSwitcher.show(detailFragment);
        }
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

    private void loadCurrentMessageTextWebView(String messageText) {
        String htmlCode = htmlCodePreparer.getHtmlCode(messageText);
        messageTextWebView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
        messageTextWebView.setBackgroundColor(Color.TRANSPARENT);
        messageTextWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

}
