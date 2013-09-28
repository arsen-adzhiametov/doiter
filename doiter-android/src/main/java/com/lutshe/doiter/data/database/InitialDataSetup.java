package com.lutshe.doiter.data.database;

import android.util.Log;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;

import java.util.HashMap;
import java.util.Map;

import static com.lutshe.doiter.model.Message.Type.FIRST;
import static com.lutshe.doiter.model.Message.Type.LAST;
import static com.lutshe.doiter.model.Message.Type.OTHER;

/**
 * Created by Arturro on 21.08.13.
 */
@EBean
public class InitialDataSetup {

    public static Goal[] initialGoals = new Goal[]{
        new Goal("Learn to ride a donkey", 0L),
                new Goal("Rate this app", 1L),
                new Goal("Go on a date", 2L),
                new Goal("Buy a car", 3L),
                new Goal("Gain a super power", 4L),
                new Goal("Kick Chuck Norris ass", 5L),
                new Goal("Don't get AIDS", 6L),
                new Goal("Learn to play guitar", 7L),
                new Goal("Learn new language", 8L),
                new Goal("Run 10 kilometers", 9L)};

    public static Map<String, Integer> goalsImages = new HashMap<String, Integer>() {
        {
            put(initialGoals[0].getImageName(), R.drawable.sample_donkey);
            put(initialGoals[1].getImageName(), R.drawable.sample_rate);
            put(initialGoals[2].getImageName(), R.drawable.sample_dating);
            put(initialGoals[3].getImageName(), R.drawable.sample_car);
            put(initialGoals[4].getImageName(), R.drawable.sample_power);
            put(initialGoals[5].getImageName(), R.drawable.sample_norris);
            put(initialGoals[6].getImageName(), R.drawable.sample_aids);
            put(initialGoals[7].getImageName(), R.drawable.sample_guitar);
            put(initialGoals[8].getImageName(), R.drawable.sample_languages);
            put(initialGoals[9].getImageName(), R.drawable.sample_running);
        }
    };

    @Bean
    GoalsDao goalsDao;
    @Bean
    MessagesDao messagesDao;

    @Trace
    public void setup() {
        for (Goal goal : initialGoals) {
            setupGoal(goal);
        }
    }

    private void setupMessage(Message message) {
        Log.d("test data setup", "adding message " + message);
        messagesDao.addMessage(message);
    }

    private void setupGoal(Goal goal) {
        Log.d("test data setup", "setting up goal " + goal);
        goalsDao.addGoal(goal);

        Message firstMessage = new Message("first msg for goal " + goal.getName(), goal.getId(), 0L);
        firstMessage.setType(FIRST);
        messagesDao.addMessage(firstMessage);

        Message lastMessage = new Message("last msg for goal " + goal.getName(), goal.getId(), null);
        lastMessage.setType(LAST);
        messagesDao.addMessage(lastMessage);

        for (int i = 1; i <= 10; i++) {
            Message someMessage = new Message("msg #" + (i+1) + " for goal '" + goal.getName() + "'", goal.getId(), Long.valueOf(i));
            someMessage.setType(OTHER);
            messagesDao.addMessage(someMessage);
        }
    }
}
