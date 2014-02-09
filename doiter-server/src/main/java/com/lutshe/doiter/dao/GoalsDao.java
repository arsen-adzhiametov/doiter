package com.lutshe.doiter.dao;

import com.lutshe.doiter.model.Goal;
import fi.evident.dalesbred.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Art
 */
@Component
public class GoalsDao {

    @Autowired
    private Database db;

    public List<Goal> getAllGoals() {
        return db.findAll(Goal.class, "select * from goals");
    }

    public Goal getGoalById(Long id) {
        return db.findUnique(Goal.class, "select * from goals where id = ?", id);
    }
}
