package com.lutshe.doiter.data.rest.clients;

import android.content.Context;
import com.lutshe.doiter.R;
import com.lutshe.doiter.dto.GoalDTO;
import org.androidannotations.annotations.EBean;

/**
 * Created by Arturro on 26.08.13.
 */
@EBean
public class GoalsRestClient extends AbstractRestClient<GoalsService> implements GoalsService {

    protected GoalsRestClient(Context context) {
        super(GoalsService.class, context.getResources().getString(R.string.serverUrl));
    }

    @Override
    public GoalDTO[] getAllGoals() {
        return service.getAllGoals();
    }

    @Override
    public GoalDTO getGoal(Long goalId) {
        return service.getGoal(goalId);
    }
}
