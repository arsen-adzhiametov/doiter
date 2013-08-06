package com.lutshe.doiter.data.model;

/**
 * Created by Artur.
 */
public class Goal {

    private final String name;
    private Long id;

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

    public void setId(Long id) {
        this.id = id;
    }
}
