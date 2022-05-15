package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.idSubtasks = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
        this.idSubtasks = new ArrayList<>();
    }

    public ArrayList<Integer> getIdSubtasks() {
        return idSubtasks;
    }

    @Override
    public String toString() {
        return getId() + ",EPIC," + getName() + "," + getStatus() + "," + getDescription() + ",";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return idSubtasks.equals(epic.idSubtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtasks);
    }
}
