package com.lutshe.doiter.views.goals.map;

import com.lutshe.doiter.views.common.Looper;

/**
 * Created by Arturro on 23.09.13.
 */
public class GoalsMapUpdater extends Looper {
    private final MapController controller;

    public GoalsMapUpdater(MapController controller) {
        super(20, "updater");
        this.controller = controller;
    }

    @Override
    protected void doAction(long dt) {
        controller.updateState(dt);
    }
}
