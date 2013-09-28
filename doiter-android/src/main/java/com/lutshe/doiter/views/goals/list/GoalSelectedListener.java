package com.lutshe.doiter.views.goals.list;

import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.views.goals.preview.GoalPreviewFragment_;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Artur
 */
@EBean
public class GoalSelectedListener implements AdapterView.OnItemClickListener {

    @Bean
    FragmentsSwitcher fragmentsSwitcher;

    @Bean
    GoalsListAdapter goalsListAdapter;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
        Goal goal = goalsListAdapter.getItem(position);
        Fragment detailFragment;
        if (goal.getStatus() == Goal.Status.OTHER) {
            detailFragment = GoalPreviewFragment_.builder().goalId(goal.getId()).build();
        } else {
            detailFragment = UserGoalDetailFragment_.builder().goalId(goal.getId()).build();
        }
        fragmentsSwitcher.show(detailFragment, true);
    }
}
