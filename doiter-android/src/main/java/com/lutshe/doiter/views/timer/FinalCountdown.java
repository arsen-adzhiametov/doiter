package com.lutshe.doiter.views.timer;

import android.os.CountDownTimer;
import android.widget.TextView;
import com.lutshe.doiter.model.Goal;
import org.joda.time.DateTime;

import java.util.HashMap;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
public class FinalCountdown extends CountDownTimer {

    public static HashMap<Long, CountDownTimer> timers = new HashMap<Long, CountDownTimer>();

    private static final long COUNTDOWN_INTERVAL = 11;
    private TextView timerView;

    private FinalCountdown(long endTime, TextView timerView) {
        super(endTime, COUNTDOWN_INTERVAL);
        this.timerView = timerView;
    }

    public static CountDownTimer getTimer(Goal goal, TextView timerView){
        CountDownTimer timer = timers.get(goal.getId());
        if (timer!=null) {
            timer.cancel();
            timers.remove(goal.getId());
        }
        long timeDiff = goal.getEndTime() - DateTime.now().getMillis();
        CountDownTimer newTimer = new FinalCountdown(timeDiff, timerView);
        timers.put(goal.getId(), newTimer);
        return newTimer;
    }

    @Override
    public void onTick(long timeLeft) {
        timerView.setText(String.valueOf(timeLeft));
    }

    @Override
    public void onFinish() {
        timerView.setText("Finish");
    }

    public static void invalidateTimers() {
        for (CountDownTimer timer : timers.values()){
            timer.cancel();
        }
    }
}
