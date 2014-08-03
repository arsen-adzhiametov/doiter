package com.lutshe.doiter.dto;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */
public class MessageDTO {

    private Long id;

    private String text;
    private long goalId;
    private Long orderIndex;
    private Type type = Type.OTHER;

    public enum Type {
        FIRST,
        LAST,
        OTHER
    }

    public MessageDTO() {}

    public MessageDTO(String text, long goalId, Long orderIndex) {
        this(null, text, goalId, orderIndex);
    }

    public MessageDTO(Long id, String text, long goalId, Long orderIndex) {
        this.id = id;
        this.text = text;
        this.goalId = goalId;
        this.orderIndex = orderIndex;
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

    public void setText(String text) {
        this.text = text;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", goalId=" + goalId +
                ", orderIndex=" + orderIndex +
                ", type=" + type +
                '}';
    }
}
