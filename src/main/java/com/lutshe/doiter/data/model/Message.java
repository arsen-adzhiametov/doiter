package com.lutshe.doiter.data.model;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class Message {

    private Long id;

    private final String text;

    private final Long userGoalId;

    public Message(String text, Long userGoalId) {
        this.text = text;
        this.userGoalId = userGoalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Long getUserGoalId() {
        return userGoalId;
    }

}
