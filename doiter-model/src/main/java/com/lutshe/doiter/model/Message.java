package com.lutshe.doiter.model;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class Message {

    private Long id;

    private  String text;
    private  long goalId;
    private Long deliveryTime;
    private Long orderIndex;
    private Type type = Type.OTHER;

    public enum Type {
        FIRST,
        LAST,
        OTHER
    }

    public Message() {}

    public Message(String text, long goalId, Long orderIndex) {
        this(null, text, goalId, orderIndex);
    }

    public Message(Long id, String text, long goalId, Long orderIndex) {
        this.id = id;
        this.text = text;
        this.goalId = goalId;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getGoalId() {
        return goalId;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", goalId=" + goalId +
                ", deliveryTime=" + deliveryTime +
                ", orderIndex=" + orderIndex +
                ", type=" + type +
                '}';
    }
}
