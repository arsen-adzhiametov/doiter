package com.doiter.services.goals;

import com.doiter.dao.GoalsDao;
import com.doiter.dao.MessagesDao;
import com.doiter.services.common.Path;
import com.doiter.services.common.Rest;
import com.doiter.services.common.Service;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: Artur
 */
@Rest("/json.v1.goals")
public class GoalService extends Service {

    @Autowired
    private MessagesDao messagesDao;
    @Autowired
    private GoalsDao goalsDao;

    @Path("/all")
    public List<Goal> getAllGoals() {
        return goalsDao.getAllGoals();
    }

    @Path("/{id}")
    public Goal getGoalById(Long id) {
        return goalsDao.getGoalById(id);
    }

    @Path("/{goalId}/messages")
    public List<Message> getAdvicesForGoal(Long id) {
        return messagesDao.getAllAdvices(id);
    }

    @Path("/{goalId}/messages/{firstMessageNum}/{numberOfMessages}")
    public List<Message> getMessagesForGoal(Long id, Long firstMessageNum, Long numberOfMessages) {
        return messagesDao.getAdvices(id, firstMessageNum, numberOfMessages);
    }
}
