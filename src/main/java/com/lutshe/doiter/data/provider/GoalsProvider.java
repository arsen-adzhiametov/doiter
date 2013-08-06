package com.lutshe.doiter.data.provider;

import com.lutshe.doiter.data.model.Goal;

/**
 * Created by Artur.
 */
public interface GoalsProvider {

    Goal[] getAllGoals();

    Goal getGoalById(Long id);

}
