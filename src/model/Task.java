package model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    public Integer id;
    public Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(getId(), task.getId()) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, getId(), status);
    }

    @Override
    public String toString() {
        return id + ",TASK," + name + "," + status + "," + description + ",";
    }
}