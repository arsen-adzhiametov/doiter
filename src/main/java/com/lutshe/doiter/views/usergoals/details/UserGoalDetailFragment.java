package com.lutshe.doiter.views.usergoals.details;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.MessagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;
import com.lutshe.doiter.data.provider.stub.MessagesProviderStub;
import com.lutshe.doiter.views.messages.MessagesListAdapter;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_details_fragment)
public class UserGoalDetailFragment extends Fragment {

    @ViewById(R.id.goalCoverDetail)
    ImageView goalCover;

    @ViewById(R.id.goalNameDetail)
    TextView goalName;

    @ViewById(R.id.userGoalMessagesList)
    ListView userGoalMessagesList;

    @Bean
    MessagesListAdapter messagesListAdapter;

    @Bean(GoalsProviderStub.class)
    GoalsProvider goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    @Bean(MessagesProviderStub.class)
    MessagesProvider messagesProvider;

    @FragmentArg
    Long goalId;

    @AfterViews
    public void bindData() {
        messagesListAdapter.initAdapter(goalId);
        userGoalMessagesList.setAdapter(messagesListAdapter);

        Goal goal = goalsProvider.getGoalById(goalId);
        goalName.setText(goal.getName());

        Bitmap bitmap = imagesProvider.getImage(goalId);
        goalCover.setImageBitmap(bitmap);
    }
}
