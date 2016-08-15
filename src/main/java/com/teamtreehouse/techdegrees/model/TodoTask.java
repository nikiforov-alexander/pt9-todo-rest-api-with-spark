package com.teamtreehouse.techdegrees.model;

public class TodoTask {
    // primary key: auto-generated
    private int id;
    // column in database VARCHAR NAME
    private String name;

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

    // only constructor
    public TodoTask(String name) {
        this.name = name;
    }

    // equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoTask todoTask = (TodoTask) o;

        if (id != todoTask.id) return false;
        return name != null ? name.equals(todoTask.name) : todoTask.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TodoTask { " +
                "id = " + id +
                ", name = '" + name + '\'' +
                " }";
    }
}
