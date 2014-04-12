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
import com.lutshe.doiter.views.BackStackable;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.usergoals.details.messages.UserGoalMessagesListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment implements UpdatableView, BackStackable {

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

    @FragmentArg Long goalId;

    @AfterViews
    public void bindData() {
        Goal goal = goalsDao.getGoal(goalId);
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        goalNameTextView.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        daysQuantityTextView.setText(String.valueOf(getDaysRemaining(goal)));
        loadCurrentMessageTextWebView(getResources().getString(R.string.message_stub));
        setTypefaceToTextViews();
    }

    @Override
    public void update() {}

    private void setTypefaceToTextViews() {
        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
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
        fragmentsSwitcher.show(UserGoalMessagesListFragment_.builder().goalId(goalId).build(), true);
    }

    @Click(R.id.prev_arrow_button)
    void showPreviousGoal(){
        if(goalsDao.getUserGoalsCount()>1){

        }
    }

    @Click(R.id.next_arrow_button)
    void showNextGoal(){
        if(goalsDao.getUserGoalsCount()>1) {
            Goal nextUserGoal = getNextUserGoal();
            Fragment detailFragment = UserGoalDetailFragment_.builder().goalId(nextUserGoal.getId()).build();
            fragmentsSwitcher.show(detailFragment, false);
        }
    }

    //TODO: should be rewritten
    private Goal getNextUserGoal(){
        Goal[] goals = goalsDao.getAllUserGoals();
        for(int i = 0; i<goals.length; i++){
            if(goals[i].getId() == goalId){
                if(i < goals.length-1) {
                    return goals[i + 1];
                }
            }
        }
        return goals[0];
    }

    private void loadCurrentMessageTextWebView(String messageText) {
        String htmlCode = htmlCodePreparer.getHtmlCode(messageText);
        messageTextWebView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
        messageTextWebView.setBackgroundColor(Color.TRANSPARENT);
        messageTextWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

}
