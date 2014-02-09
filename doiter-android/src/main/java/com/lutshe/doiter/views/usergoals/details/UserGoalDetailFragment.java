package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;
import org.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.views.BackStackable;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.timer.FinalCountdown;
import com.lutshe.doiter.views.usergoals.details.messages.UserGoalMessagesListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment implements UpdatableView, BackStackable {

    @ViewById(R.id.remaining)TextView remainingTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.goal_name_3_screen)TextView goalNameTextView;
    @ViewById(R.id.back_to_all_button_text)TextView backToAllButtonTextView;
    @ViewById(R.id.goal_cover_3_screen)ImageView goalCover;
    @ViewById(R.id.message_number)TextView messageNumberTextView;
    @ViewById(R.id.message_text)TextView messageTextTextView;
    @ViewById(R.id.text_time_countdown)TextView timerView;

    @Bean GoalsDao goalsDao;
    @Bean(ImagesProviderImpl.class)ImagesProvider imagesProvider;
    @Bean FragmentsSwitcher fragmentsSwitcher;

    @FragmentArg Long goalId;

    @AfterViews
    public void bindData() {
        Goal goal = goalsDao.getGoal(goalId);
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        goalNameTextView.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        daysQuantityTextView.setText(String.valueOf(getDaysRemaining(goal)));

        FinalCountdown.getTimer(goal, timerView).start();
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
        messageTextTextView.setTypeface(typeface);
    }

    private long getDaysRemaining(Goal goal) {
        long timeDiff = goal.getEndTime() - DateTime.now().getMillis();
        long daysRemaining = timeDiff/(1000*60*60*24);
        return daysRemaining;
    }

    @Click(R.id.more_tips_button)
    void showAllTips(){
        fragmentsSwitcher.show(UserGoalMessagesListFragment_.builder().goalId(goalId).build(), true);
    }

}
