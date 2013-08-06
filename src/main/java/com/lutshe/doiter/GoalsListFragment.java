package com.lutshe.doiter;


import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.GridView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.views.goals.GoalSelectedListener;
import com.lutshe.doiter.views.goals.GoalsListAdapter;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

@EFragment(R.layout.goals_list_fragment)
public class GoalsListFragment extends Fragment {

    @ViewById(R.id.goalsList)
    GridView goalsList;

    @Bean
    GoalsListAdapter goalsListAdapter;
    @Bean(GoalSelectedListener.class)
    AdapterView.OnItemClickListener goalClickListener;

    @AfterViews
    void bindList() {
        goalsList.setAdapter(goalsListAdapter);
        goalsList.setOnItemClickListener(goalClickListener);
    }
}
