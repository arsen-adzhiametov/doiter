package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.usergoals.messages.UserGoalMessagesListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import org.androidannotations.annotations.*;
import org.joda.time.DateTime;

import static com.lutshe.doiter.AchievementTimeConstants.MILLISECONDS_IN_DAY;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment implements UpdatableView {

    @ViewById(R.id.user_goal_detail_fragment_view) UserGoalDetailLayout userGoalDetailLayout;

    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;
    @Bean(ImagesProviderImpl.class)ImagesProvider imagesProvider;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean HtmlCodePreparer htmlCodePreparer;

    @FragmentArg Long goalId;

    private Goal[] userGoals;
    private int currentGoalIndex;

    @AfterViews
    public void bindData() {
        userGoals = goalsDao.getAllUserGoalsSortedByStatus();
        resolveCurrentGoalIndexAndId();
        Goal goal = goalsDao.getGoal(goalId);
        loadGoalCoverImage(goal);
        loadTextViews(goal);
        loadCurrentMessage(goal);
        disableNavigationIfOnlyOneUserGoal();
    }

    private void resolveCurrentGoalIndexAndId() {
        if (goalId == null) {
            currentGoalIndex = 0;
            goalId = userGoals[currentGoalIndex].getId();
        } else {
            for(int i = 0; i < userGoals.length; i++){
                if(userGoals[i].getId().equals(goalId)){
                    currentGoalIndex = i;
                    break;
                }
            }
        }
    }

    private void loadGoalCoverImage(Goal goal) {
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        userGoalDetailLayout.showGoalCover(bitmap);
    }

    private void loadTextViews(Goal goal) {
        userGoalDetailLayout.showGoalName(goal.getName());
        int daysRemaining = getDaysRemaining(goal);
        userGoalDetailLayout.showDaysRemaining(daysRemaining);
    }

    private void loadCurrentMessage(Goal goal) {
        Message currentMessage = messagesDao.getMessage(goal.getId(), goal.getLastMessageIndex());
        String htmlCode = htmlCodePreparer.getHtmlCode(currentMessage.getText());
        userGoalDetailLayout.showCurrentMessageOnWebView(htmlCode);
    }

    private void disableNavigationIfOnlyOneUserGoal() {
        if (userGoals.length == 1){
            userGoalDetailLayout.hideNavigationButtons();
        }
    }

    @Override
    public void update() {}

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

}
