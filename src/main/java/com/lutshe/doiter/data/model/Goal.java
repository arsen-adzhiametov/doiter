package com.lutshe.doiter.data.model;

/**
 * Created by Artur.
 */
public class Goal {

    private final String name;
    private Long id;
    private Long endTime;

    public Goal(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
    public Long getEndTime() {
        return endTime;
    }
}
