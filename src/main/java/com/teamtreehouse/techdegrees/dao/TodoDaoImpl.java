package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.TodoTask;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class TodoDaoImpl implements TodoDao {
    // our session factory database object
    private final Sql2o sql2o;
    // constructor initializing our db session factory
    public TodoDaoImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<TodoTask> findAll() {
        // open connection: session
        try (Connection connection = sql2o.open()) {
            // create SQL query "SELECT * FROM todos
            return connection.createQuery(
                    "SELECT * FROM todos"
                   ).executeAndFetch(TodoTask.class);
        }

    }

    @Override
    public TodoTask findById(int id) {
        // try open connection
        try (Connection connection = sql2o.open()) {
            // create simple sql SELECT query, add parameter and fetch
            // todoTask
            return connection.createQuery(
                    "SELECT * FROM todos WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(TodoTask.class);
        }
    }

    @Override
    public int save(TodoTask todoTask) throws DaoException {
        // We insert here all fields, except id, because it will be
        // auto-generated
        String sqlQuery =
                "INSERT INTO todos(name, completed, edited) " +
                        "VALUES(:name, :completed, :edited)";
        // open connection
        try (Connection connection = sql2o.open()) {
            // get auto-generated id
            int id = (int) connection.createQuery(sqlQuery)
                    .bind(todoTask)
                    .executeUpdate()
                    .getKey();
            // set new id for our TodoTask object
            todoTask.setId(id);
            // return id, just for easier testing purposes
            return id;
        } catch (Sql2oException sql2oException) {
            // throw exception if something went wrong
            throw new DaoException(sql2oException, sql2oException.getMessage());
        }
    }

    @Override
    public void delete(TodoTask todoTask) {

    }

    @Override
    public void update(TodoTask todoTask) {

    }
}
