package com.lutshe.doiter.model;

/**
 * Created by Artur.
 */
public class Goal {

    private Long id;
    private String name;

    public Goal() {}

    public Goal(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public String getImageName() {
        return getId() + ".png";
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", name='" + name + '\'' +
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
