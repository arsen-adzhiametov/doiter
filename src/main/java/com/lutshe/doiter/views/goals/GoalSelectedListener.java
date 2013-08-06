package com.lutshe.doiter.views.goals;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.lutshe.doiter.GoalDetailFragment;
import com.lutshe.doiter.GoalDetailFragment_;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.model.Goal;

/**
 * Created by Artur
 */
@EBean
public class GoalSelectedListener implements AdapterView.OnItemClickListener {

    @RootContext
    Context context;

    @Bean
    GoalsListAdapter goalsListAdapter;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
        Goal goal = goalsListAdapter.getItem(position);
        Toast.makeText(context, goal.getName() + " clicked", Toast.LENGTH_SHORT).show();

        GoalDetailFragment goalDetail = new GoalDetailFragment_();
        Bundle bundle = new Bundle();
        bundle.putLong("goalId", goal.getId());
        goalDetail.setArguments(bundle);
        replaceFragment(goalDetail);

    }

    void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
