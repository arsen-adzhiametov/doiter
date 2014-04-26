package com.lutshe.doiter.views.goals.map;

import android.app.Fragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.model.Goal;
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
    private int scrollSpeedXDecrease = 0;
    private int scrollSpeedYDecrease = 0;
    private int scrollSpeedX = 0;
    private int scrollSpeedY = 0;

    private int screenWidth;
    private int screenHeight;

    private float currentOffsetX;
    private float currentOffsetY;

    private GoalsMapView goalsMapView;
    private GoalsDao goalsDao;

    public MapController(FragmentsSwitcher fragmentsSwitcher, GoalsMapView goalsMapView,
                         ImagesProvider imagesProvider, GoalsDao goalsDao) {
        this.fragmentsSwitcher = fragmentsSwitcher;
        this.goalsMapView = goalsMapView;
        this.map = new Map(imagesProvider, goalsDao.getAllGoals());
        this.goalsDao = goalsDao;
    }

    public float getCurrentOffsetX() {
        return currentOffsetX;
    }

    public float getCurrentOffsetY() {
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
        if (scrollSpeedX == 0 && scrollSpeedY == 0) {
            return;
        }

        if (Math.signum(scrollSpeedXDecrease) == Math.signum(scrollSpeedX)) {
            scrollSpeedX -= scrollSpeedXDecrease;
        } else {
            scrollSpeedX = 0;
        }

        if (Math.signum(scrollSpeedYDecrease) == Math.signum(scrollSpeedY)) {
            scrollSpeedY -= scrollSpeedYDecrease;
        } else {
            scrollSpeedY = 0;
        }

        long dx = (scrollSpeedX * dt) >> 10;
        long dy = (scrollSpeedY * dt) >> 10;

        addOffsets(dx, dy);
    }

    @Override
    public void onScroll(float dx, float dy, long dt) {
        addOffsets(dx, dy);
    }

    private synchronized void addOffsets(float dx, float dy) {
        if (dx != 0) {
            currentOffsetX = trim(currentOffsetX + dx, getMapWidth());
        }

        if (dy != 0) {
            currentOffsetY = trim(currentOffsetY + dy, getMapHeight());
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
        x = trim(x - currentOffsetX, getMapWidth());
        y = trim(y - currentOffsetY, getMapHeight());
        Long goalId = map.findGoalUnder(x, y).getId();
        Goal goal = goalsDao.getGoal(goalId);
        if (goal == null) return;

        goalsMapView.startShowingBg();

        Fragment nextFragment;
        if (goal.getStatus() == Goal.Status.OTHER) {
            nextFragment = GoalPreviewFragment_.builder().goalId(goal.getId()).build();
            EasyTracker tracker = EasyTracker.getInstance(fragmentsSwitcher.getActivity());
            tracker.send(MapBuilder.createEvent("goal_selection", "goal_previewing", goal.getName().toString(), 1L).build());
        } else {
            nextFragment = UserGoalDetailFragment_.builder().goalId(goal.getId()).build();
        }
        fragmentsSwitcher.show(nextFragment);
    }

    @Override
    public void onEventStarted() {
        scrollSpeedX = scrollSpeedY = 0;
    }

    @Override
    public void onEventFinished(float dx, float dy, long time) {
        scrollSpeedX = (int) (dx * 1024 / time);
        scrollSpeedY = (int) (dy * 1024 / time);
        scrollSpeedXDecrease = scrollSpeedX / 40;
        scrollSpeedYDecrease = scrollSpeedY / 40;
    }

    public void init() {
        map.init(screenWidth, screenHeight);
    }
}
