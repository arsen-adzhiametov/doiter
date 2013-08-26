package com.lutshe.doiter.views.timer;

import android.os.CountDownTimer;
import android.widget.TextView;
import org.joda.time.DateTime;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class FinalCountdown extends CountDownTimer {

    private static final long COUNTDOWN_INTERVAL = 471;
    private TextView timerView;

    private FinalCountdown(long endTime, TextView timerView) {
        super(endTime, COUNTDOWN_INTERVAL);
        this.timerView = timerView;
    }

    public static FinalCountdown getTimer(long goalEndTime, TextView timerView){
        long timeDiff = goalEndTime - DateTime.now().getMillis();
        return new FinalCountdown(timeDiff, timerView);
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
