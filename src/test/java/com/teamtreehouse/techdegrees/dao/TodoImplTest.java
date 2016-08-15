package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.TodoTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class TodoImplTest {
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
        return new TodoTask("TodoTask 1");
    }

    @Test
    public void addingEntrySetsId() throws Exception {
        // Arrange: create new todoTask
        TodoTask todoTask = newTodoTask();

        // When todoTask is added to DAO
        int newTodoId = todoDaoImpl.save(todoTask);

        // Then his index is auto-incremented to 1
        assertEquals(1, newTodoId);
    }


}