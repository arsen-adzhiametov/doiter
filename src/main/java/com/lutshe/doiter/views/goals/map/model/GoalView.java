package com.lutshe.doiter.views.goals.map.model;

import com.lutshe.doiter.data.model.Goal;

/**
 * Created by Arturro on 22.09.13.
 */
public class GoalView {
    private final ViewType type;
    private final Goal goal;
    private int x;
    private int y;

    public GoalView(ViewType type, Goal goal) {
        this.type = type;
        this.goal = goal;
    }

    public enum ViewType {
        SQUARE_GOAL(150, 150),
        TALL_GOAL(124, 170),
        WIDE_GOAL(170, 124);

        final int w, h;

        ViewType(int w, int h) {
            this.w = w;
            this.h = h;
        }

        public GoalView create(Goal goal) {
            return new GoalView(this, goal);
        }
    }

    public int getWidth() {
       return type.w;
    }
    public int getHeight() {
        return type.h;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Goal getGoal() {
        return goal;
    }
}
