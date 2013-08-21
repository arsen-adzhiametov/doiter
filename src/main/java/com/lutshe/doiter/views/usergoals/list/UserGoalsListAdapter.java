package com.lutshe.doiter.views.usergoals.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.RootContext;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.GoalsProvider;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;
import com.lutshe.doiter.views.goals.list.GoalItemView;
import com.lutshe.doiter.views.goals.list.GoalItemView_;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
@EBean
public class UserGoalsListAdapter extends BaseAdapter{

    private Goal[] userGoals;

    @Bean(GoalsProviderStub.class)
    GoalsProvider goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    @Bean
    GoalsDao goalsDao;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        userGoals = goalsDao.getAllUserGoals();
    }

    @Override
    public int getCount() {
        return userGoals.length;
    }

    @Override
    public Goal getItem(int position) {
        return userGoals[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @ItemClick
    public void listItemClicked(Goal goal) {
        Toast toast = new Toast(context);
        toast.setText(goal.getName());
        toast.show();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        GoalItemView goalView = (GoalItemView) convertView;

        if (goalView == null) {
            goalView = GoalItemView_.build(context);
        }
        long goalId = getItem(position).getId();
        Goal goal = goalsProvider.getGoalById(goalId);
        Bitmap bitmap = imagesProvider.getImage(goal.getId());
        return goalView.bind(goal, bitmap);
    }
}
