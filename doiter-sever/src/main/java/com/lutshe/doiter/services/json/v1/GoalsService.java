package com.lutshe.doiter.services.json.v1;

import com.lutshe.doiter.dao.GoalsDao;
import com.lutshe.doiter.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @Author: Art
 */
@Path("/json.v1.goals")
@Component
public class GoalsService {

    @Autowired
    private GoalsDao goalsDao;

    @Autowired
    private MessagesDao messagesDao;

    @GET
    @Path("/all")
    @Produces("application/json;charset=UTF-8")
    public List<Goal> getAllGoals() {
        return goalsDao.getAllGoals();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public Goal getGoalById(@PathParam("id") Long id) {
        return goalsDao.getGoalById(id);
    }

    @GET
    @Path("/{goalId}/messages")
    @Produces("application/json;charset=UTF-8")
    public List<Message> getAllMessagesForGoal(@PathParam("goalId") Long id) {
        return messagesDao.getAllMessagesForGoal(id);
    }

    @GET
    @Path("/{goalId}/messages/{firstMessageNum}/{numberOfMessages}")
    @Produces("application/json;charset=UTF-8")
    public List<Message> getMessagesForGoal(
            @PathParam("goalId") Long id,
            @PathParam("firstMessageNum") Long firstMessageNum,
            @PathParam("numberOfMessages") Long numberOfMessages) {
        return messagesDao.getMessagesForGoal(id, firstMessageNum, numberOfMessages);
    }
}
