package com.lutshe.doiter.data.rest.clients;

import com.lutshe.doiter.model.Goal;

import retrofit.http.GET;

/**
 * Created by Arturro on 22.08.13.
 */
public interface GoalsService {

    @GET("/json.v1.goals/all")
    Goal[] getAllGoals();

    @GET("/json.v1.goals/{goalId}")
    Goal getGoal(Long goalId);
}
