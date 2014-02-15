package com.lutshe.doiter.dao;

import com.lutshe.doiter.model.Message;
import fi.evident.dalesbred.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Art
 */
@Component
public class MessagesDao {

    @Autowired
    private Database db;

    public List<Message> getAllMessagesForGoal(Long id) {
        return db.findAll(Message.class, "select * from messages where goal_id = ?", id);
    }

    public List<Message> getMessagesForGoal(Long id, Long firstMessageNum, Long numberOfMessages) {
        return db.findAll(Message.class, "select * from messages where goal_id = ? limit ?, ?", id, firstMessageNum, numberOfMessages);
    }

    public void saveMessage(Message message) {
        db.update("insert into messages(goal_id, text, order_index) values (?, ?, ?);", message.getGoalId(), message.getText(), message.getOrderIndex());
    }
}
