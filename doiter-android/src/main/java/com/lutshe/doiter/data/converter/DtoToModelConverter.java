package com.lutshe.doiter.data.converter;

import com.lutshe.doiter.dto.GoalDTO;
import com.lutshe.doiter.dto.MessageDTO;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;

/**
 * Created by Arsen Adzhiametov on 03-Aug-14 in IntelliJ IDEA.
 */
public class DtoToModelConverter {

    public static Goal convertToGoalModel(GoalDTO dto){
        Goal model = new Goal();
        model.setId(dto.getId());
        model.setName(dto.getName());
        return model;
    }

    public static Message convertToMessageModel(MessageDTO dto){
        Message model = new Message();
        model.setId(dto.getId());
        model.setGoalId(dto.getGoalId());
        model.setText(dto.getText());
        model.setOrderIndex(dto.getOrderIndex());
        model.setType(dto.getType());
        return model;
    }
}
