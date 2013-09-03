package com.lutshe.doiter.data.model;

/**
 * Created by Artur.
 */
public class Goal {

    private final String name;
    private Long id;
    private Long endTime;
    private Long lastMessageIndex;
    private Status status = Status.OTHER;

    public enum Status {
        ACTIVE,
        INACTIVE,
        OTHER
    }

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

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Long getLastMessageIndex() {
        return lastMessageIndex;
    }

    public void setLastMessageIndex(Long lastMessageIndex) {
        this.lastMessageIndex = lastMessageIndex;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", endTime=" + endTime +
                ", lastMessageIndex=" + lastMessageIndex +
                ", status=" + status +
                '}';
    }
}
