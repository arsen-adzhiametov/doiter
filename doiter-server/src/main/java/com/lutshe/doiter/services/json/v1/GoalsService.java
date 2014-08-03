package com.lutshe.doiter.services.json.v1;

import com.lutshe.doiter.dao.GoalsDao;
import com.lutshe.doiter.dao.MessagesDao;
import com.lutshe.doiter.dto.GoalDTO;
import com.lutshe.doiter.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static com.lutshe.doiter.converter.ModelToDtoConverter.*;

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
    public List<GoalDTO> getAllGoals() {
        return convertToGoalDto(goalsDao.getAllGoals());
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public GoalDTO getGoalById(@PathParam("id") Long id) {
        return convertToGoalDto(goalsDao.getGoalById(id));
    }

    @GET
    @Path("/{goalId}/messages")
    @Produces("application/json;charset=UTF-8")
    public List<MessageDTO> getAllMessagesForGoal(@PathParam("goalId") Long id) {
        return convertToMessageDto(messagesDao.getAllMessagesForGoal(id));
    }

    @GET
    @Path("/{goalId}/messages/{firstMessageNum}/{numberOfMessages}")
    @Produces("application/json;charset=UTF-8")
    public List<MessageDTO> getMessagesForGoal(
            @PathParam("goalId") Long id,
            @PathParam("firstMessageNum") Long firstMessageNum,
            @PathParam("numberOfMessages") Long numberOfMessages) {
        return convertToMessageDto(messagesDao.getMessagesForGoal(id, firstMessageNum, numberOfMessages));
    }
}
