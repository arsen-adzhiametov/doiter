package com.lutshe.doiter.data.rest.clients;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Goal;

import retrofit.RestAdapter;

/**
 * Created by Arturro on 26.08.13.
 */
@EBean
public class GoalsRestClient implements GoalsService {

    private static GoalsService goalsService;

    private static synchronized GoalsService service() {
        if (goalsService == null) {
            RestAdapter adapter = new RestAdapter.Builder().setServer("http://192.168.1.17:8844/api/json/v1").setDebug(true).build();
            goalsService = adapter.create(GoalsService.class);
        }
        return goalsService;
    }

    @Override
    public Goal[] getAllGoals() {
        return service().getAllGoals();
    }

    @Override
    public Goal getGoal(Long goalId) {
        return service().getGoal(goalId);
    }
}
