package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    public ArrayList<Integer> idSubtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.idSubtasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + super.name + '\'' +
                ", description='" + super.description + '\'' +
                ", id=" + super.id +
                ", status='" + super.status + '\'' +
                ", idSubtasks=" + idSubtasks +
                '}';
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
