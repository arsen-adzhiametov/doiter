package com.lutshe.doiter.views.goals.map.model;

import android.util.Log;

import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;

/**
 * Created by Arturro on 22.09.13.
 */
public class Map {
    private static final String TAG = Map.class.getName();

    private static final double ROWS_PER_SCREEN = 3;

    private static final double MIN_ROWS = 3;
    private static final double MIN_COLS = 2;

    public int cellWidth;
    public int cellHeight;
    public int borderSize;

    private final Goal[] goals;

    private GoalView[][] goalsGrid;
    private final ImagesProvider imagesProvider;

    public Map(ImagesProvider imagesProvider, Goal... goals) {
        Log.d(TAG, "have " + goals.length + " goals");
        this.goals = goals;
        this.imagesProvider = imagesProvider;
    }

    public void init(float screenWidth, int screenHeight) {
        cellHeight = cellWidth = (int) Math.floor(screenHeight / ROWS_PER_SCREEN);
        borderSize = cellWidth / 5;

        int availableSquareSize = (int) Math.floor(Math.sqrt(goals.length));
        if (availableSquareSize >= MIN_ROWS) {
            generateGrid(availableSquareSize, availableSquareSize, goals);
        } else {
            generateGrid((int) MIN_ROWS, (int) Math.max(MIN_COLS, (goals.length / MIN_ROWS) + 1), goals);
        }
    }

    private void generateGrid(int rows, int cols, Goal[] goals) {
        if (rows % 2 == 1) rows *= 2;
        Log.d(TAG, "creating grid " + rows + "x" + cols);
        goalsGrid = new GoalView[rows][cols];
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int viewId = (row * cols) + col;
                GoalView view = createGoalView(goals[viewId % goals.length]);
                view.setX(col * cellWidth + (cellWidth - view.getWidth()) / 2);
                view.setY(row * cellHeight + (cellHeight - view.getHeight()) / 2);

                if (row % 2 == 1) {
                    // all odd rows are shifted to the left.
                    view.setX(view.getX() - cellWidth / 2);
                }

                goalsGrid[row][col] = view;
            }
        }
    }

    private GoalView createGoalView(Goal goal) {
        return GoalView.create(goal, cellWidth, cellHeight, borderSize, imagesProvider);
    }

    public GoalView[][] getGoalsGrid() {
        return goalsGrid;
    }

    public int getWidth() {
        return goalsGrid[0].length * cellWidth;
    }

    public int getHeight() {
        return goalsGrid.length * cellHeight;
    }

    public Goal findGoalUnder(double x, double y) {
        for (GoalView[] row : goalsGrid) {
            for (GoalView goalView : row) {
                if (hitTest(x, y, goalView)) {
                    return goalView.getGoal();
                }
            }
        }
        return null;
    }

    private boolean hitTest(double x, double y, GoalView view) {
        return view.getX() <= x && x <= view.getX() + view.getWidth() && view.getY() <= y && y <= view.getHeight() + view.getY();
    }
}
