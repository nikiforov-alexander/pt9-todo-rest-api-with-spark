package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDaoImpl;
import com.teamtreehouse.techdegrees.model.TodoTask;
import com.teamtreehouse.techdegrees.testing.ApiClient;
import com.teamtreehouse.techdegrees.testing.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AppTest {
    // server port
    private static final String PORT = "4568";

    // jdbc_url for setting up testing in memory H2 db
    private static final String JDBC_URL = "jdbc:h2:mem:testing";

    // connection: will be open and close for each request
    private Connection connection;

    // ApiClient class, with which requests will be modeled
    private ApiClient apiClient;

    // Gson object to convert from and back from TodoTask class
    private Gson gson;

    // Dao with CRUDs of database
    private TodoDao todoDao;

    // Test todo task will be the first one in db
    private TodoTask firstTestTodoTask =
            new TodoTask("Todo 1", false, false);

    @BeforeClass
    public static void startServer() {
        // run our App with other than in real App port and JDBC url
        // testing database
        String[] args = {PORT, JDBC_URL};
        App.main(args);

        // wait before Spark server is up
        Spark.awaitInitialization();
    }

    // Stop Spark Server after all tests
    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        // set connection string from JDBC_URL and initial sql script
        // before each test
        // that will create table todos for us
        String connectionString = JDBC_URL
                + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'";

        // create our sql2o session factory
        Sql2o sql2o = new Sql2o(connectionString,"","");

        // open connection
        connection = sql2o.open();

        // set up our DAO with sql2o
        todoDao = new TodoDaoImpl(sql2o);

        // initialize Gson
        gson = new Gson();

        // initialize ApiClient connecting to server
        apiClient = new ApiClient("http://localhost:" + PORT);

    }

    @After
    public void tearDown() throws Exception {
        // close connection to sql2o, after each test
        connection.close();
    }

    @Test
    public void addingTodoReturnsCreatedStatus() throws Exception {
        // Arrange:
        // create Map to be converted in JSON in request
        Map<String, Object> todoTaskMap = new HashMap<>();
        // put all properties of test TodoTask object there
        todoTaskMap.put("name", firstTestTodoTask.getName());
        todoTaskMap.put("edited", firstTestTodoTask.isEdited());
        todoTaskMap.put("completed", firstTestTodoTask.isCompleted());
        // because after saving to db id of the test task will be set to 1
        // we set it here manually
        firstTestTodoTask.setId(1);

        // Act, Assert:
        // When POST request is made to "/api/v1/todos", to add
        // new TodoTask, with JSON created from map above
        ApiResponse apiResponse =
                apiClient.request(
                        "POST",
                        "/api/v1/todos",
                        gson.toJson(todoTaskMap)
                );

        // status should be 201
        assertEquals(201, apiResponse.getStatus());

        // the only task from response converted from JSON should be
        // exactly our firstTestTodoTask
        TodoTask onlyTaskFromResponse = gson.fromJson(
                apiResponse.getBody(),
                TodoTask.class);
        assertEquals(firstTestTodoTask, onlyTaskFromResponse);
    }

    @Test
    public void getRequestToIndexPageReturnsListOfTodos() throws Exception {
        // Arrange:
        // create Map to be converted in JSON in request
        Map<String, Object> todoTaskMap = new HashMap<>();
        // put all properties of test TodoTask object there
        todoTaskMap.put("name", firstTestTodoTask.getName());
        todoTaskMap.put("edited", firstTestTodoTask.isEdited());
        todoTaskMap.put("completed", firstTestTodoTask.isCompleted());
        // because after saving to db id of the test task will be set to 1
        // we set it here manually
        firstTestTodoTask.setId(1);
        // finally we add two test todoTasks to db: should be OK: they will
        // have different ids
        apiClient.request(
                "POST",
                "/api/v1/todos",
                gson.toJson(todoTaskMap)
        );
        apiClient.request(
                "POST",
                "/api/v1/todos",
                gson.toJson(todoTaskMap)
        );

        // Act, Assert:
        // When GET request is made to "/api/v1/todos"
        ApiResponse apiResponse =
                apiClient.request(
                        "GET",
                        "/api/v1/todos"
                );
        // here we convert tasks to array
        TodoTask[] todoTasks =
                gson.fromJson(
                        apiResponse.getBody(),
                        TodoTask[].class
                );

        // Then: status should be OK 200
        assertEquals(200, apiResponse.getStatus());
        //      length of todoTasks should be one
        assertEquals(2, todoTasks.length);
        //      First element should be our test task
        assertEquals(firstTestTodoTask, todoTasks[0]);
    }
}