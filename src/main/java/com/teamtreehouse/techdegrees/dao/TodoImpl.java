package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class TodoImpl implements TodoDao {
    // our session factory database object
    private final Sql2o sql2o;
    // constructor initializing our db session factory
    public TodoImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Todo> findAll() {
        // open connection: session
        try (Connection connection = sql2o.open()) {
            // create SQL query "SELECT * FROM todos
            return connection.createQuery(
                    "SELECT * FROM todos"
                   ).executeAndFetch(Todo.class);
        }

    }

    @Override
    public Todo findById() {
        return null;
    }

    @Override
    public void save(Todo todo) {

    }

    @Override
    public void delete(Todo todo) {

    }

    @Override
    public void update(Todo todo) {

    }
}
