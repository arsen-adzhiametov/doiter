package com.lutshe.doiter.views.goals.list;


import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.GridView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.UpdatableView;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.goals_list_fragment)
public class GoalsListFragment extends Fragment implements UpdatableView {

    @ViewById(R.id.goalList)
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

    @Override
    public void update() {
        goalsListAdapter.notifyDataSetChanged();
    }
}
