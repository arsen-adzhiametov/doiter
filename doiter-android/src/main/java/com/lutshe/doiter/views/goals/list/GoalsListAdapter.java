package com.lutshe.doiter.views.goals.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;

/**
 * Created by Artur
 */
@EBean
public class GoalsListAdapter extends BaseAdapter {

    private Goal[] goals;

    @Bean
    GoalsDao goalsProvider;

    @Bean(ImagesProviderImpl.class)
    ImagesProvider imagesProvider;

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return goals().length;
    }

    @Override
    public Goal getItem(int position) {
        return goals()[position];
    }

    private Goal[] goals() {
        if (goals == null) {
            loadGoals();
        }
        return goals;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        GoalItemView goalView = (GoalItemView) convertView;

        if (goalView == null) {
            goalView = GoalItemView_.build(context);
        }

        Goal goal = getItem(position);
        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        return goalView.bind(goal, bitmap);
    }

    @Override
    public void notifyDataSetChanged() {
        loadGoals();
        super.notifyDataSetChanged();
    }

    private void loadGoals() {
        goals = goalsProvider.getAllGoals();
    }
}
