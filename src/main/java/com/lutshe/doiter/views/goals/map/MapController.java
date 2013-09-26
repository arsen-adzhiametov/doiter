package com.lutshe.doiter.views.goals.map;

import android.app.Fragment;

import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;
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

    // per 1024 ms
    private int scrollSpeedX = 0;
    private int scrollSpeedY = 0;

    private int screenWidth;
    private int screenHeight;

    private float currentOffsetX;
    private float currentOffsetY;

    public MapController(FragmentsSwitcher fragmentsSwitcher, ImagesProvider imagesProvider,  Goal... goals) {
        this.fragmentsSwitcher = fragmentsSwitcher;
        this.map = new Map(imagesProvider, goals);
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
//        scrollSpeedX -= (5 * dt) >> 10;
//        scrollSpeedY -= (5 * dt) >> 10;
//
//        if (scrollSpeedX < 0) scrollSpeedX = 0;
//        if (scrollSpeedY < 0) scrollSpeedY = 0;
//
//        if (scrollSpeedX == 0 && scrollSpeedY == 0) {
//            return;
//        }
//
//        long dx = (scrollSpeedX * dt) >> 10;
//        long dy = (scrollSpeedY * dt) >> 10;
//
//        addOffsets(dx, dy);
    }

    @Override
    public void onScroll(float dx, float dy, long dt) {
        addOffsets(dx, dy);
    }

    private void addOffsets(float dx, float dy) {
        if (dx != 0) {
            currentOffsetX = trim(currentOffsetX + dx, screenWidth);
        }

        if (dy != 0) {
            currentOffsetY = trim(currentOffsetY + dy, screenHeight);
        }
    }

    private float trim(float value, float maxValue) {
        if (value > maxValue) {
            return value - maxValue;
        }
        if (value < 0) {
            return maxValue - Math.abs(value);
        }
        return value;
    }

    @Override
    public void onClick(float x, float y) {
        x = trim(x - currentOffsetX, screenWidth);
        y = trim(y- currentOffsetY, screenHeight);
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
        scrollSpeedX = (int) (dx * 1024 / time);
        scrollSpeedY = (int) (dy * 1024 / time);
    }

    public int getCellWidth() {
        return map.cellWidth;
    }
    public int getCellHeight() {
        return map.cellHeight;
    }

    public void init() {
        map.init(screenWidth, screenHeight);
    }
}
