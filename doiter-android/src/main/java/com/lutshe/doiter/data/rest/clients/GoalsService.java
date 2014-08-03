package com.lutshe.doiter.data.rest.clients;

import com.lutshe.doiter.dto.GoalDTO;

import retrofit.http.GET;

/**
 * Created by Arturro on 22.08.13.
 */
public interface GoalsService {

    @GET("/json.v1.goals/all")
    GoalDTO[] getAllGoals();

    @GET("/json.v1.goals/{goalId}")
    GoalDTO getGoal(Long goalId);
}
