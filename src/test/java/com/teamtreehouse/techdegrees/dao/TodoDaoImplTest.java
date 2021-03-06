package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.TodoTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TodoDaoImplTest {
    // our implementation of DAO object
    private TodoDaoImpl todoDaoImpl;
    // sql2o connection object
    private Connection connection;

    // set up server, open connection
    @Before
    public void setUp() throws Exception {
        // jdbc URL with testing database in memory
        String jdbcURL =
                "jdbc:h2:mem:testing;" +
                        "INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        // create Database 'session Factory'
        Sql2o sql2o = new Sql2o(jdbcURL, "", "");
        // pass session factory to TodoDaoImpl
        todoDaoImpl = new TodoDaoImpl(sql2o);
        // Keep connection open through entire test so that it is not wiped out
        connection = sql2o.open();
    }

    // Close database connection
    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    private TodoTask newTodoTask() {
        return new TodoTask("TodoTask 1", false);
    }

    @Test
    public void savingTodoSetsId() throws Exception {
        // Arrange: create new todoTask
        TodoTask todoTask = newTodoTask();

        // When todoTask is added to DAO
        int newTodoId = todoDaoImpl.save(todoTask);

        // Then his index is auto-incremented to 1
        assertEquals(1, newTodoId);
    }

    @Test
    public void existingTodosCanBeFoundById() throws Exception {
        // Arrange: create new todoTask
        TodoTask todoTask = newTodoTask();
        // add it to test db
        todoDaoImpl.save(todoTask);

        // Act, and Assert
        // When findById method is called
        TodoTask foundTodoTask = todoDaoImpl.findById(todoTask.getId());

        // found todoTask should be equal to original todoTask
        assertEquals(todoTask, foundTodoTask);
    }

    @Test
    public void existingTodosCanBeFoundByFindAll() throws Exception {
        // Arrange: create new todoTask
        TodoTask todoTask = newTodoTask();
        // add it to test db
        todoDaoImpl.save(todoTask);

        // Act, and Assert
        // When findAll method is called
        List<TodoTask> todoTaskList = todoDaoImpl.findAll();

        // todoTaskList should be equal to one
        assertEquals(1, todoTaskList.size());
    }

    @Test
    public void updateTodosWorks() throws Exception {
        // Arrange: create new todoTask
        TodoTask todoTask = newTodoTask();
        // add it to test db
        todoDaoImpl.save(todoTask);
        // change name of todoTask
        todoTask.setName("new name");

        // Act, and Assert
        // When update method is called
        todoDaoImpl.update(todoTask);

        // found todoTask should be equal to original todoTask
        TodoTask foundTodoTask = todoDaoImpl.findById(todoTask.getId());
        assertEquals(todoTask, foundTodoTask);
    }

    @Test
    public void deletingTodoWorks() throws Exception {
        // Arrange: create new todoTask
        TodoTask todoTask = newTodoTask();
        // add it to test db
        todoDaoImpl.save(todoTask);

        // Act, and Assert
        // When delete method is called
        todoDaoImpl.delete(todoTask);

        // Then: only task that was added and deleted had id = 1, so
        // the task with id = 1 should not be found
        assertEquals(null, todoDaoImpl.findById(1));

    }
}