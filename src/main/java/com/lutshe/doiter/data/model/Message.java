package com.lutshe.doiter.data.model;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class Message {

    private long id;

    private final String text;
    private final long userGoalId;
    private Long deliveryTime;
    private Long orderIndex;
    private Type type = Type.OTHER;

    public enum Type {
        FIRST,
        LAST,
        OTHER
    }

    public Message(long id, String text, long userGoalId, Long orderIndex) {
        this.id = id;
        this.text = text;
        this.userGoalId = userGoalId;
        this.orderIndex = orderIndex;
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

    public void setType(Type type) {
        this.type = type;
    }
    public Type getType() {
        return type;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }
}
