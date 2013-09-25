package com.lutshe.doiter.views.goals.map;

import android.app.Fragment;

import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.views.common.TouchEventsListener;
import com.lutshe.doiter.views.goals.map.model.GoalView;
import com.lutshe.doiter.views.goals.map.model.Map;
import com.lutshe.doiter.views.goals.preview.GoalPreviewFragment_;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arturro on 22.09.13.
 */
public class MapController implements TouchEventsListener {
    private final FragmentsSwitcher fragmentsSwitcher;
    private final Map map;

    private int scrollSpeedX = 15;
    private int scrollSpeedY = -8;

    private int screenWidth;
    private int screenHeight;

    private float currentOffsetX;
    private float currentOffsetY;

    public MapController(FragmentsSwitcher fragmentsSwitcher, Map map) {
        this.fragmentsSwitcher = fragmentsSwitcher;
        this.map = map;
    }

    public double getCurrentOffsetX() {
        return currentOffsetX;
    }

    public double getCurrentOffsetY() {
        return currentOffsetY;
    }

    public int getMapWidth() {
        return map.getWidth();
    }

    public int getMapHeight() {
        return map.getHeight();
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public GoalView[][] getGoalViews() {
        return map.getGoalsGrid();
    }

    public void updateState(long dt) {
        addOffsets((scrollSpeedX  * dt ) >> 10, (scrollSpeedY * dt) >> 10);
    }

    @Override
    public void onScroll(float dx, float dy, long dt) {
        addOffsets(dx, dy);
    }

    private void addOffsets(float dx, float dy) {
        if (dx != 0) {
            currentOffsetX = trim(currentOffsetX + dx, 0, screenWidth);
        }

        if (dy != 0) {
            currentOffsetY = trim(currentOffsetY + dy, 0, screenHeight);
        }
    }

    private float trim(float value, float minValue, float maxValue) {
        if (value > maxValue) {
            return value - maxValue;
        }
        if (value < minValue) {
            return maxValue - value;
        }
        return value;
    }

    @Override
    public void onClick(float x, float y) {
        x = trim(x - currentOffsetX, 0, screenWidth);
        y = trim(y - currentOffsetY, 0, screenHeight);
        Goal goal = map.findGoalUnder(x, y);
        if (goal == null) return;

        Fragment detailFragment;
        if (goal.getStatus() == Goal.Status.OTHER) {
            detailFragment = GoalPreviewFragment_.builder().goalId(goal.getId()).build();
        } else {
            detailFragment = UserGoalDetailFragment_.builder().goalId(goal.getId()).build();
        }
        fragmentsSwitcher.show(detailFragment, true);
    }

    @Override
    public void onEventFinished(float dx, float dy, long time) {
        scrollSpeedX = 0;
        scrollSpeedY = 0;
    }

    public int getCellWidth() {
        return Map.CELL_WIDTH;
    }
    public int getCellHeight() {
        return Map.CELL_HEIGHT;
    }
}
