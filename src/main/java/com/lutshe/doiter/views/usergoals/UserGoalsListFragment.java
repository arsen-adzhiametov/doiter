package com.lutshe.doiter.views.usergoals;

import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.goals.list.GoalsListFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EFragment(R.layout.user_goal_list_fragment)
public class UserGoalsListFragment extends Fragment {

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
        fragmentsSwitcher.show(R.id.fragment_container, GoalsListFragment_.builder().build());
    }
}
