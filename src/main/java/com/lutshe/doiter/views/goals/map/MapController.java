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
    private static final String TAG = MapController.class.getName();
    private static final int SLOWNESS_FACTOR = 1; // full stop in one second

    private final FragmentsSwitcher fragmentsSwitcher;
    private final Map map;

    private int scrollSpeedX = 15;
    private int scrollSpeedY = -8;

    private int screenWidth;
    private int screenHeight;

    private double currentOffsetX;
    private double currentOffsetY;

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
        if (scrollSpeedX != 0) {
            currentOffsetX += (scrollSpeedX  * dt ) >> 10;
            if (currentOffsetX > screenWidth) {
                currentOffsetX -= screenWidth;
            }
            if (currentOffsetX < 0) {
                currentOffsetX = screenWidth - currentOffsetX;
            }
        }

        if (scrollSpeedY != 0) {
            currentOffsetY += (scrollSpeedY * dt) >> 10;
            if (currentOffsetY > screenHeight) {
                currentOffsetY -= screenHeight;
            }
            if (currentOffsetY < 0) {
                currentOffsetY = screenHeight - currentOffsetY;
            }
        }
    }

    @Override
    public void onScroll(float dx, float dy, long dt) {
        scrollSpeedX = (int) ((dx * 1024) / dt);
        scrollSpeedY = (int) ((dy * 1024) / dt);
    }

    @Override
    public void onClick(float x, float y) {
        Goal goal = map.findGoalUnder(x - currentOffsetX, y - currentOffsetY);
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
}
