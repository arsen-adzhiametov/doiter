package com.lutshe.doiter.data.model;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

public class UserGoal {

    private Long goalId;

    private Long endTime;

    public UserGoal(Long goalId, Long endTime) {
        this.goalId = goalId;
        this.endTime = endTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }

}
