package com.lutshe.doiter.views.goals.list;

import android.view.View;
import android.widget.AdapterView;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.views.goals.preview.GoalPreviewFragment;
import com.lutshe.doiter.views.goals.preview.GoalPreviewFragment_;
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

        GoalPreviewFragment detailFragment = GoalPreviewFragment_.builder().goalId(goal.getId()).build();
        fragmentsSwitcher.show(detailFragment, true);
    }
}
