package com.lutshe.doiter.data.database;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.res.BooleanRes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.lutshe.doiter.AchievementTimeConstants.DB_RELATED_TAG;

/**
 * Created by Arturro on 21.08.13.
 */
@EBean
public class InitialDataSetup {

    public static Goal[] initialGoals = new Goal[]{
                new Goal("Меньше Сидеть в Интернете", 0L),
                new Goal("Убраться в Квартире", 1L),
                new Goal("Научиться Готовить", 2L),
                new Goal("Завести Домашнее Животное", 3L),
                new Goal("Научиться Отдыхать", 4L),
                new Goal("Провести Отпуск в Зимбабве", 5L),
                new Goal("Уволиться с Работы", 6L),
                new Goal("Расстаться с Девушкой", 7L),
                new Goal("Бросить Курить", 8L),
                new Goal("Выйти Замуж", 9L),
                new Goal("Научиться Танцевать", 10L),
                new Goal("Улучшить Карму", 11L),
                new Goal("Разыграть Друга", 12L),
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
            put(initialGoals[11].getImageName(), R.drawable.goal11);
            put(initialGoals[12].getImageName(), R.drawable.goal12);
        }
    };

    @Bean
    GoalsDao goalsDao;
    @Bean
    MessagesDao messagesDao;
    @RootContext
    Context context;

    @BooleanRes(resName = "makeNotDeliveredMessagesReadableForTest")
    boolean deliverAllMessagesOnSetup;

    @Trace(tag = DB_RELATED_TAG, level = Log.INFO)
    public void setup() {
        for (Goal goal : initialGoals) {
            setupGoal(goal);
        }
    }

    private void setupGoal(Goal goal) {
        Log.d("test data setup", "setting up goal " + goal);
        String[] messages = getMessagesArray(goal);

        if (messages == null) {
            return;
        }

        Message first = new Message(messages[0], goal.getId(), 0L);
        first.setType(Message.Type.FIRST);
        messagesDao.addMessage(first);

        Message last = new Message(messages[messages.length-1], goal.getId(), null);
        last.setType(Message.Type.LAST);
        messagesDao.addMessage(last);

        if (deliverAllMessagesOnSetup) {
            messagesDao.updateMessageDeliveryTime(messagesDao.getMessage(goal.getId(), Message.Type.LAST).getId());
            messagesDao.updateMessageDeliveryTime(messagesDao.getMessage(goal.getId(), Message.Type.FIRST).getId());
        }

        for (int i = 1; i < messages.length - 1; i ++) {
            Message other = new Message(messages[i], goal.getId(), Long.valueOf(i));
            messagesDao.addMessage(other);
            if (deliverAllMessagesOnSetup) {
                messagesDao.updateMessageDeliveryTime(messagesDao.getMessage(goal.getId(), Long.valueOf(i)).getId());
            }
        }

        goalsDao.addGoal(goal);
    }

    private String[] getMessagesArray(Goal goal) {
        try {
            Resources resources = context.getResources();
            Field field = R.array.class.getField("goal" + goal.getId());
            int resourceId = field.getInt(null);
            return resources.getStringArray(resourceId);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }
}
