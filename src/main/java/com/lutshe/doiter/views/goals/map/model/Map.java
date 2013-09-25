package com.lutshe.doiter.views.goals.map.model;

import android.util.Log;

import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.views.util.Randomdom;

/**
 * Created by Arturro on 22.09.13.
 */
public class Map {
    private static final String TAG = Map.class.getName();

    private static final int ROWS_PER_SCREEN = 4;
    private static final int COLS_PER_SCREEN = 3;

    private static final int BORDER = 10;

    public static final int CELL_WIDTH = GoalView.ViewType.WIDE_GOAL.w + BORDER * 2;
    public static final int CELL_HEIGHT = GoalView.ViewType.TALL_GOAL.h + BORDER * 2;

    private GoalView[][] goalsGrid;

    public Map(Goal... goals) {
        Log.d(TAG, "have " + goals.length + " goals");

        int availableSquareSize = (int) Math.floor(Math.sqrt(goals.length));
        if (availableSquareSize >= ROWS_PER_SCREEN) {
            generateGrid(availableSquareSize, availableSquareSize, goals);
        } else {
            generateGrid(ROWS_PER_SCREEN, Math.max(COLS_PER_SCREEN, (goals.length / ROWS_PER_SCREEN) + 1), goals);
        }
    }

    private GoalView createGoalView(Goal goal) {
        GoalView.ViewType viewType = Randomdom.choose(GoalView.ViewType.values());
        return viewType.create(goal);
    }

    private void generateGrid(int rows, int cols, Goal[] goals) {
        Log.d(TAG, "creating grid " + rows + "x" + cols);
        goalsGrid = new GoalView[rows][cols];
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int viewId = (row * cols) + col;
                GoalView view = createGoalView(goals[viewId % goals.length]);
                view.setX(col * CELL_WIDTH + (CELL_WIDTH - view.getWidth() - 2 * BORDER) / 2);
                view.setY(row * CELL_HEIGHT + (CELL_HEIGHT - view.getHeight() - 2 * BORDER) / 2);
                goalsGrid[row][col] = view;
            }
        }
        System.out.println("s");
    }

    public GoalView[][] getGoalsGrid() {
        return goalsGrid;
    }

    public int getWidth() {
        return goalsGrid[0].length * CELL_WIDTH;
    }

    public int getHeight() {
        return goalsGrid.length * CELL_HEIGHT;
    }

    public Goal findGoalUnder(double x, double y) {
        for (GoalView[] row : goalsGrid) {
            for (GoalView goalView : row) {
                if (hitTest(x, y, goalView)) {
                    return goalView.getGoal();
                }
            }
        }
        // TODO: test siblings if moving out of borders is allowed
        return null;
    }

    private boolean hitTest(double x, double y, GoalView view) {
        return view.getX() <= x && x <= view.getX() + view.getWidth() && view.getY() <= y && y <= view.getHeight() + view.getY();
    }
}
