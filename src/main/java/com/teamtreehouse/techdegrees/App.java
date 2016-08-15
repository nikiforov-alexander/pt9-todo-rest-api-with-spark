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
        get("/api/v1/todos", "application/json",
                (request, response) -> todoDao.findAll(),
                gson::toJson);

        // get course by id
        get("/api/v1/todos/:id","application/json", (request, response) -> {
            int id = Integer.parseInt(request.params("id"));
            TodoTask todoTask = todoDao.findById(id);
            if (todoTask == null) {
                throw new ApiError(404, "Could not find todoTask with id " + id);
            }
            return todoTask;
        }, gson::toJson );

        // save new task: post request to main page
        post("/api/v1/todos", "application/json", (request, response) -> {
            // get todo task from JSON request made in Angular
            TodoTask todoTask = gson.fromJson(request.body(), TodoTask.class);

            // save Todo task to db
            todoDao.save(todoTask);

            // set response status to CREATED
            response.status(201);

            // return todo task
            return todoTask;
        }, gson::toJson);

        // after each request we make response of type application/json
        after((request, response) -> response.type("application/json"));

        // exception handler
        exception(ApiError.class, (exception, request, response) -> {
            ApiError apiError = (ApiError) exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", apiError.getStatus());
            jsonMap.put("errorMessage", apiError.getMessage());
            response.type("application/json");
            response.status(apiError.getStatus());
            response.body(gson.toJson(jsonMap));
        });
    }

}
