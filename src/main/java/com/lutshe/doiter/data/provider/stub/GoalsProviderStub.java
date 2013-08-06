package com.lutshe.doiter.data.provider.stub;

import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.GoalsProvider;

/**
 * Created by Artur
 */
@EBean
public class GoalsProviderStub implements GoalsProvider {
    @Override
    public Goal[] getAllGoals() {
        return new Goal[] {
                new Goal("Learn to ride a donkey", 0L),
                new Goal("Rate this app", 1L),
                new Goal("Go on a date", 2L),
                new Goal("Buy a car", 3L),
                new Goal("Gain a super power", 4L),
                new Goal("Kick Chuck Norris ass", 5L),
                new Goal("Don't get AIDS", 6L),
                new Goal("Learn to play guitar", 7L),
                new Goal("Learn new language", 8L),
                new Goal("Run 10 kilometers", 9L),
        };
    }

    @Override
    public Goal getGoalById(Long id) {
        return getAllGoals()[id.intValue()];
    }
}
