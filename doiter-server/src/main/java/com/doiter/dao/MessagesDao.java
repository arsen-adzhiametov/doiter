package com.doiter.dao;

import com.lutshe.doiter.model.Message;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessagesDao {

    @Select("select * from messages where goal_id = #{param1} limit #{param2}, #{param3}")
    List<Message> getAdvices(Long goalId, Long first, Long count);

    @Select("select * from messages where goal_id = #{goalId}")
    List<Message> getAllAdvices(Long goalId);
}
