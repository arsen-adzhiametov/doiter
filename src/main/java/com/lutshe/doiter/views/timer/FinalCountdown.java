package com.lutshe.doiter.views.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class FinalCountdown extends CountDownTimer {

    private static final long COUNTDOWN_INTERVAL = 471;
    private TextView timerView;

    public FinalCountdown(long endTime, TextView timerView) {
        super(endTime - System.currentTimeMillis(), COUNTDOWN_INTERVAL);
        this.timerView = timerView;
    }

    @Override
    public void onTick(long timeLeft) {
        timerView.setText(String.valueOf(timeLeft));
    }

    @Override
    public void onFinish() {
        timerView.setText("Finish");
    }
}
