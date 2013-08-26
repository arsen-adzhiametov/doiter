package com.lutshe.doiter.preloaders;

import android.app.AlarmManager;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.rest.clients.GoalsRestClient;
import com.lutshe.doiter.data.rest.clients.GoalsService;
import com.lutshe.doiter.data.rest.clients.MessagesRestClient;
import com.lutshe.doiter.data.rest.clients.MessagesService;

/**
 * Created by Arturro on 26.08.13.
 */
@EBean
class GoalsLoader implements Loader {
    public static final long LOADING_INTERVAL = AlarmManager.INTERVAL_DAY * 7;

    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;

    @Bean GoalsRestClient goalsRestClient;
    @Bean MessagesRestClient messagesRestClient;

    @Override
    public long getLoadingInterval() {
        return LOADING_INTERVAL;
    }

    @Trace
    @Override
    public void load() {
        Goal[] allGoals = goalsRestClient.getAllGoals();
        for (Goal goal : allGoals) {
            storeGoal(goal);
        }
    }

    private void storeGoal(Goal goal) {
        if (goalsDao.getGoal(goal.getId()) == null) {
            goalsDao.addGoal(goal);
            loadMessagesForGoal(goal);
        }
    }

    private void loadMessagesForGoal(Goal goal) {
        Message[] messages = messagesRestClient.getMessagesForGoal(goal.getId(), 0L, 10L);
        for (Message message : messages) {
            messagesDao.addMessage(message);
        }
    }
}
