package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.dao.TodoImpl;
import com.teamtreehouse.techdegrees.exception.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import java.util.List;

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
        TodoDao todoDao = new TodoImpl(sql2o);
        // autowire Gson, create Gson
        Gson gson = new Gson();

        // index page: all todos are printed
        get("/", "application/json",
            (request, response) -> todoDao.findAll(),
                gson::toJson);

        // get course by id
        get("/courses/:id","application/json", (request, response) -> {
            int id = Integer.parseInt(request.params("id"));
            Todo todo = todoDao.findById(id);
            if (todo == null) {
                throw new ApiError(404, "Could not find course with id " + id);
            }
            return todo;
        }, gson::toJson );

        // after each request we make response of type application/json
        after((request, response) -> response.type("application/json"));
    }

}
