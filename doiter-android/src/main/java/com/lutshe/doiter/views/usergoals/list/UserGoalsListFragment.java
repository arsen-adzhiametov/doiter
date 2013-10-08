package com.lutshe.doiter.views.usergoals.list;

import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.goals.map.GoalsMapFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EFragment(R.layout.user_goal_list_fragment)
public class UserGoalsListFragment extends Fragment implements UpdatableView {

    @ViewById(R.id.userGoalsList)
    ListView userGoalsList;

    @Bean
    FragmentsSwitcher fragmentsSwitcher;

    @Bean
    UserGoalsListAdapter userGoalsListAdapter;

    @Bean(UserGoalSelectedListener.class)
    AdapterView.OnItemClickListener userGoalClickListener;

    @AfterViews
    void bindList() {
        userGoalsList.setAdapter(userGoalsListAdapter);
        userGoalsList.setOnItemClickListener(userGoalClickListener);
    }

    @Click(R.id.showAllGoalsButton)
    void showAllGoals(){
        fragmentsSwitcher.show(GoalsMapFragment_.builder().build(), true);
    }

    @Override
    public void update() {
        userGoalsListAdapter.notifyDataSetChanged();
    }
}
