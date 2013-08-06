package com.lutshe.doiter;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

@EFragment(R.layout.goal_detail_fragment)
public class GoalDetailFragment extends Fragment {

    @ViewById(R.id.goalNameDetail)
    TextView goalName;

    @ViewById(R.id.goalCoverDetail)
    ImageView goalCover;

    @Bean(GoalsProviderStub.class)
    GoalsProvider goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    private Long goalId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        goalId = bundle.getLong("goalId");
    }

    @AfterViews
    public void bindData(){
        Goal goal = goalsProvider.getGoalById(goalId);
        Bitmap bitmap = imagesProvider.getImage(goalId);
        goalName.setText(goal.getName());
        goalCover.setImageBitmap(bitmap);
    }
}
