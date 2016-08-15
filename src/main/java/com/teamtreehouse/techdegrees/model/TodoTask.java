package com.teamtreehouse.techdegrees.model;

public class TodoTask {
    // primary key: auto-generated
    private int id;
    // column in database VARCHAR NAME
    private String name;
    // column in database BOOLEAN
    private boolean completed;
    // column in database BOOLEAN
    private boolean edited;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    // only constructor
    public TodoTask(String name, boolean completed, boolean edited) {
        this.name = name;
        this.completed = completed;
        this.edited = true;
    }

    // equals and hashcode and ToString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoTask todoTask = (TodoTask) o;

        if (id != todoTask.id) return false;
        if (completed != todoTask.completed) return false;
        return name != null ? name.equals(todoTask.name) : todoTask.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TodoTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                ", edited=" + edited +
                '}';
    }
}
