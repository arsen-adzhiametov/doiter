package com.lutshe.doiter.views.goals.list;

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
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.stub.ImagesProviderStub;

/**
 * Created by Artur
 */
@EBean
public class GoalsListAdapter extends BaseAdapter {

    private Goal[] goals;

    @Bean
    GoalsDao goalsProvider;

    @Bean(ImagesProviderStub.class)
    ImagesProvider imagesProvider;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        goals = goalsProvider.getAllGoals();
    }

    @Override
    public int getCount() {
        return goals.length;
    }

    @Override
    public Goal getItem(int position) {
        return goals[position];
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

        Goal goal = getItem(position);
        Bitmap bitmap = imagesProvider.getImage(goal.getId());
        return goalView.bind(goal, bitmap);
    }
}
