package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDaoImpl;
import com.teamtreehouse.techdegrees.exception.ApiError;
import com.teamtreehouse.techdegrees.model.TodoTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    // Added static final String with api context
    static final String API_CONTEXT = "/api/v1";

    public static void main(String[] args) {
        // jdbc URL, will be for now embedded db
        // in 'data' directory
        String jdbcURL = "jdbc:h2:./data/my-todos";
        // introduced for testing:
        // App will run with args in testing
        if (args.length > 0 ) {
            if (args.length != 2) {
                System.out.println("java Api <port> <jdbcUrl>");
                System.exit(1);
            }
            port(Integer.parseInt(args[0]));
            jdbcURL = args[1];
        }
        // initialize db from script
        String connectionString = String.format(
                "%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'",
                jdbcURL);
        // create sql2o object: aka session factory with connection
        // string provided
        Sql2o sql2o =
                new Sql2o(connectionString,"","");
        // autowire DAO, connect with sql2o
        TodoDao todoDao = new TodoDaoImpl(sql2o);
        // autowire Gson, create Gson
        Gson gson = new Gson();
        // static files location so that we can upload our angular to website
        staticFileLocation("/public");

        // index page: all todos are printed
        get(API_CONTEXT + "/todos", "application/json",
                (request, response) -> todoDao.findAll(),
                gson::toJson);

        // save new task: post request to main page
        post(API_CONTEXT + "/todos", "application/json", (request, response) -> {
            // get todoTask from JSON request made in Angular
            TodoTask todoTask = gson.fromJson(request.body(), TodoTask.class);

            // save TodoTask to db
            todoDao.save(todoTask);

            // set response status to CREATED
            response.status(201);

            // return todoTask
            return todoTask;
        }, gson::toJson);

        // update task: put request to main page with :id
        put(API_CONTEXT + "/todos/:id", "application/json", (request, response) -> {
            // get id parameter from request
            int id = Integer.parseInt(
                    request.params("id")
            );

            // check if todoTask with this id exist
            if (todoDao.findById(id) == null) {
                throw new ApiError(404,
                        "Could not find todoTask with id " + id);
            }

            // get todoTask from JSON request made in Angular
            TodoTask todoTask = gson.fromJson(request.body(), TodoTask.class);

            // set todoTask id
            todoTask.setId(id);

            // save TodoTask to db
            todoDao.update(todoTask);

            // set response status to OK
            response.status(200);

            // return todoTask
            return todoTask;
        }, gson::toJson);

        // delete task: delete request to main page with :id
        delete(API_CONTEXT + "/todos/:id", "application/json", (request, response) -> {
            // get id parameter from request
            int id = Integer.parseInt(
                    request.params("id")
            );

            // get todoTask by id
            TodoTask todoTask = todoDao.findById(id);

            // check if todoTask with this id exist
            if (todoTask == null) {
                throw new ApiError(404,
                        "Could not find todoTask with id " + id);
            }

            // delete TodoTask from db
            todoDao.delete(todoTask);

            // set response status to OK/empty body
            response.status(204);

            // return null/empty body
            return null;
        }, gson::toJson);

        // after each request we make response of type application/json
        after((request, response) -> response.type("application/json"));

        // exception handler for object not found
        exception(ApiError.class, (exception, request, response) -> {
            ApiError apiError = (ApiError) exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", apiError.getStatus());
            jsonMap.put("errorMessage", apiError.getMessage());
            response.type("application/json");
            response.status(apiError.getStatus());
            response.body(gson.toJson(jsonMap));
        });
        // exception handler for parse error
        exception(NumberFormatException.class, (exception, request, response) -> {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", 500);
            jsonMap.put("errorMessage", "Error Parsing Id of todo");
            response.type("application/json");
            response.status(500);
            response.body(gson.toJson(jsonMap));
        });
    }

}
