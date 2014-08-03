package com.lutshe.doiter.converter;

import com.lutshe.doiter.dto.GoalDTO;
import com.lutshe.doiter.dto.MessageDTO;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen Adzhiametov on 03-Aug-14 in IntelliJ IDEA.
 */
public class ModelToDtoConverter {

    public static GoalDTO convertToGoalDto(Goal model){
        GoalDTO dto = new GoalDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    }

    public static List<GoalDTO> convertToGoalDto(List<Goal> list){
        List<GoalDTO> goalDTOs = new ArrayList<>();
        for (Goal goal : list){
            goalDTOs.add(convertToGoalDto(goal));
        }
        return goalDTOs;
    }

    public static MessageDTO convertToMessageDto(Message model){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(model.getId());
        messageDTO.setGoalId(model.getGoalId());
        messageDTO.setText(model.getText());
        messageDTO.setOrderIndex(model.getOrderIndex());
        messageDTO.setType(model.getType());
        return messageDTO;
    }

    public static List<MessageDTO> convertToMessageDto(List<Message> list){
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message message : list){
            messageDTOs.add(convertToMessageDto(message));
        }
        return messageDTOs;
    }
}
