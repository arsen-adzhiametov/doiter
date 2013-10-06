package com.lutshe.doiter.model;

/**
 * Created by Artur.
 */
public class Goal {

    private Long id;
    private String name;
    private Long endTime;
    private Long lastMessageIndex;
    private Status status = Status.OTHER;
    private Long imageCoverId;

    public enum Status {
        ACTIVE,
        INACTIVE,
        OTHER
    }

    public Goal() {}

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

    public String getImageName() {
        return getId() + ".png";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goal goal = (Goal) o;

        if (!id.equals(goal.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
