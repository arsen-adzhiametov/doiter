package com.lutshe.doiter.data.database;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.stub.GoalsProviderStub;

import static com.lutshe.doiter.data.model.Message.Type.FIRST;
import static com.lutshe.doiter.data.model.Message.Type.LAST;

/**
 * Created by Arturro on 21.08.13.
 */
@EBean
public class TestDataSetup {

    @Bean
    GoalsDao goalsDao;
    @Bean
    MessagesDao messagesDao;

    public void setup() {
        GoalsProviderStub goalsProvider = new GoalsProviderStub();

        for (Goal goal : goalsProvider.getAllGoals()) {
            setupGoal(goal);
        }
    }

    private void setupGoal(Goal goal) {
        goalsDao.addGoal(goal);

        Message firstMessage = new Message(goal.getId() * 10 + 1, "some msg", goal.getId());
        firstMessage.setType(FIRST);
        messagesDao.addMessage(firstMessage);

        Message lastMessage = new Message(goal.getId() * 10 + 2, "some last msg", goal.getId());
        lastMessage.setType(LAST);
        messagesDao.addMessage(lastMessage);
    }
}
