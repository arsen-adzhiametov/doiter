package com.lutshe.doiter.data.model;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class Message {

    private long id;

    private final String text;

    private Long deliveryTime;

    private final long userGoalId;

    public Message(long id, String text, long userGoalId) {
        this.id = id;
        this.text = text;
        this.userGoalId = userGoalId;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getUserGoalId() {
        return userGoalId;
    }

    public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
