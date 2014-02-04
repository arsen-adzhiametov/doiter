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
                new Goal("Меньше сидеть в интернете", 0L),
                new Goal("Убраться в квартире", 1L),
                new Goal("Научиться готовить", 2L),
                new Goal("Завести домашнее животное", 3L),
                new Goal("Научиться отдыхать", 4L),
                new Goal("Провести отпуск в Зимбабве", 5L),
                new Goal("уволиться с работы", 6L),
                new Goal("Расстаться с девушкой", 7L),
                new Goal("Бросить курить", 8L),
                new Goal("Выйти замуж", 9L),
                new Goal("Научиться танцевать", 10L),
    };

    public static Map<String, Integer> goalsImages = new HashMap<String, Integer>() {
        {
            put(initialGoals[0].getImageName(), R.drawable.goal0);
            put(initialGoals[1].getImageName(), R.drawable.goal1);
            put(initialGoals[2].getImageName(), R.drawable.goal2);
            put(initialGoals[3].getImageName(), R.drawable.goal3);
            put(initialGoals[4].getImageName(), R.drawable.goal4);
            put(initialGoals[5].getImageName(), R.drawable.goal5);
            put(initialGoals[6].getImageName(), R.drawable.goal6);
            put(initialGoals[7].getImageName(), R.drawable.goal7);
            put(initialGoals[8].getImageName(), R.drawable.goal8);
            put(initialGoals[9].getImageName(), R.drawable.goal9);
            put(initialGoals[10].getImageName(), R.drawable.goal10);
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
