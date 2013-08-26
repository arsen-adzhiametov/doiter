package com.lutshe.doiter.data.rest.clients;

import com.lutshe.doiter.data.model.Goal;

import retrofit.http.GET;

/**
 * Created by Arturro on 22.08.13.
 */
public interface GoalsService {

    @GET("/goals/all")
    Goal[] getAllGoals();

    @GET("/goals/{goalId}")
    Goal getGoal(Long goalId);
}
