package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.timer.FinalCountdown;
import com.lutshe.doiter.views.usergoals.details.messages.MessagesListAdapter;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment implements UpdatableView {

    @ViewById(R.id.remaining)TextView remainingTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.goal_name_3_screen)TextView goalNameTextView;
    @ViewById(R.id.back_to_all_button_text)TextView backToAllButtonTextView;
    @ViewById(R.id.goal_cover_3_screen)ImageView goalCover;
    @ViewById(R.id.user_goal_messages_list)ListView userGoalMessagesList;
    @ViewById(R.id.text_time_countdown)TextView timerView;

    @Bean MessagesListAdapter messagesListAdapter;
    @Bean GoalsDao goalsDao;
    @Bean(ImagesProviderImpl.class)ImagesProvider imagesProvider;

    @FragmentArg Long goalId;

    @AfterViews
    public void bindData() {
        messagesListAdapter.initAdapter(goalId);
        userGoalMessagesList.setAdapter(messagesListAdapter);

        Goal goal = goalsDao.getGoal(goalId);
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        goalNameTextView.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
        daysQuantityTextView.setText(String.valueOf(getDaysRemaining(goal)));

        FinalCountdown.getTimer(goal, timerView).start();
        setTypefaceToTextViews();
    }

    @Override
    public void update() {
        messagesListAdapter.notifyDataSetChanged();
    }

    private void setTypefaceToTextViews() {
        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        remainingTextView.setTypeface(typeface);
        daysQuantityTextView.setTypeface(typeface);
        daysTextTextView.setTypeface(typeface);
        goalNameTextView.setTypeface(typeface);
        backToAllButtonTextView.setTypeface(typeface);
    }

    private long getDaysRemaining(Goal goal) {
        long timeDiff = goal.getEndTime() - DateTime.now().getMillis();
        long daysRemaining = timeDiff/(1000*60*60*24);
        return daysRemaining;
    }

}
