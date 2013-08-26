package com.lutshe.doiter.preloaders;

import android.app.AlarmManager;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.rest.clients.MessagesRestClient;

/**
 * Created by Arturro on 24.08.13.
 */
@EBean
public class MessagesLoader implements Loader {
    public static final long TWO_DAYS_INTERVAL = AlarmManager.INTERVAL_DAY * 2;

    @Bean MessagesDao messagesDao;
    @Bean GoalsDao goalsDao;

    @Bean MessagesRestClient messagesRestClient;

    @Override
    public long getLoadingInterval() {
        return TWO_DAYS_INTERVAL;
    }

    @Trace
    @Override
    public void load() {
        Goal[] userGoals = goalsDao.getActiveUserGoals();
        if (userGoals == null) {
            return;
        }

        for (Goal userGoal : userGoals) {
            Long maxAvailable = messagesDao.getMaxMessageIdForGoal(userGoal.getId());
            Long lastReceived = userGoal.getLastMessageIndex();

            if (maxAvailable - lastReceived >= 6) {
                continue;
            }

            long loadFromIndex = Math.max(maxAvailable, lastReceived) + 1;
            loadMessagesForGoal(userGoal, loadFromIndex, lastReceived);
        }
    }

    private void loadMessagesForGoal(Goal userGoal, long loadFromIndex, Long lastReceived) {
        Message[] messages = messagesRestClient.getMessagesForGoal(userGoal.getId(), loadFromIndex, 10L - (loadFromIndex - lastReceived));
        for (Message message : messages) {
            message.setId((Long) null);
            messagesDao.addMessage(message);
        }
    }
}
