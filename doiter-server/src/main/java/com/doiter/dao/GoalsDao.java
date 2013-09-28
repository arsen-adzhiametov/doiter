package com.doiter.dao;

import com.lutshe.doiter.model.Goal;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * User: Artur
 */
public interface GoalsDao {

    @Select("select * from goals")
    List<Goal> getAllGoals();

    @Select("select * from goals where id = #{id}")
    Goal getGoalById(Long id);
}
