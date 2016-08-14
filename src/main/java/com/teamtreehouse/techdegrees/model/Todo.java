package com.teamtreehouse.techdegrees.model;

public class Todo {
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
    public Todo(String name) {
        this.name = name;
    }

    // equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        if (id != todo.id) return false;
        return name != null ? name.equals(todo.name) : todo.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Todo { " +
                "id = " + id +
                ", name = '" + name + '\'' +
                " }";
    }
}
