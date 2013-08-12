package com.lutshe.doiter.data.model;


/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

public class Message {

    private Long id;

    private String text;

    private Long userGoalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserGoalId() {
        return userGoalId;
    }

    public void setUserGoalId(Long userGoalId) {
        this.userGoalId = userGoalId;
    }
}
