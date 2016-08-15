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

import java.util.HashMap;
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

    // Test todoTask will be the first one in db
    private TodoTask firstTestTodoTask =
            new TodoTask("Todo 1", false, false);

    // test Model Map that will be used to make test POST request with
    // first todoTask
    private Map<String, Object> todoTaskMapWithFirstTestTodoTask =
            new HashMap<>();

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

    // used in setUp method, and used in `update` tests to create
    // map to be sent with PUT request with changed data
    private void fillTodoTaskMapWithDataFromTestTodoTask() {
        // fill testTodoTaskMap with data from test TodoTask
        todoTaskMapWithFirstTestTodoTask.put("name",
                firstTestTodoTask.getName());
        todoTaskMapWithFirstTestTodoTask.put("edited",
                firstTestTodoTask.isEdited());
        todoTaskMapWithFirstTestTodoTask.put("completed",
                firstTestTodoTask.isCompleted());
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

        // fill todoTaskMap with data from firstTestTodoTask. Ready
        // to make POST request to add new task to db
        // important: id will be 0, so that id is auto-generated to 1
        fillTodoTaskMapWithDataFromTestTodoTask();

        // because after saving to db id of the test task will be set to 1
        // we set it here manually, to test the returned from db todoTask
        // with this one
        firstTestTodoTask.setId(1);
    }

    @After
    public void tearDown() throws Exception {
        // close connection to sql2o, after each test
        connection.close();
    }

    @Test
    public void addingTodoReturnsCreatedStatus() throws Exception {
        // Arrange:
        // In this test todoTaskMapWithFirstTestTodoTask and
        // firstTestTodoTask will be used.
        // They are generated in setUp method, ready to be sent as request

        // Act, Assert:
        // When POST request is made to "/api/v1/todos", to add
        // new TodoTask, with JSON created from
        // todoTaskMapWithFirstTestTodoTask
        ApiResponse apiResponse =
                apiClient.request(
                        "POST",
                        "/api/v1/todos",
                        gson.toJson(todoTaskMapWithFirstTestTodoTask)
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
        // like before we use firstTodoTask and
        // todoTaskMapWithFirstTestTodoTask.
        // Also we add two same test todoTasks to db: should be OK: they will
        // have different ids
        apiClient.request(
                "POST",
                "/api/v1/todos",
                gson.toJson(todoTaskMapWithFirstTestTodoTask)
        );
        apiClient.request(
                "POST",
                "/api/v1/todos",
                gson.toJson(todoTaskMapWithFirstTestTodoTask)
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

    @Test
    public void updatingTodoTaskActuallyUpdatesTodoTask() throws Exception {
        // Arrange:
        //   1. Here firstTestTodoTask and todoTaskMapWithFirstTestTodoTask will
        //   be used to add test todoTask to db.
        //   2. We add firstTestTodoTask to database with POST below
        apiClient.request(
                "POST",
                "/api/v1/todos",
                gson.toJson(todoTaskMapWithFirstTestTodoTask)
        );
        //   3. Then we change name of the firstTodoTask
        firstTestTodoTask.setName("new name");
        //   4. fill the taskMap with changed firstTestTodoTask data, to
        //   make PUT request
        fillTodoTaskMapWithDataFromTestTodoTask();

        // Act, Assert:
        // When PUT request is made to "/api/v1/todos/1", to update
        //  test TodoTask, with JSON created from map of test todoTask
        //  with new name
        // (Here we manually write 1 as id).
        ApiResponse apiResponse =
                apiClient.request(
                        "PUT",
                        "/api/v1/todos/1",
                        gson.toJson(todoTaskMapWithFirstTestTodoTask)
                );

        // Then:
        // - status should be 200
        assertEquals(200, apiResponse.getStatus());

        // the task from response converted from JSON should be
        // exactly our firstTestTodoTask
        TodoTask updatedTodoTask = gson.fromJson(
                apiResponse.getBody(),
                TodoTask.class);
        assertEquals(firstTestTodoTask, updatedTodoTask);
    }

}