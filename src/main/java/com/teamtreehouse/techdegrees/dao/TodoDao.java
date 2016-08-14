package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {
    List<Todo> findAll();
    Todo findById(int id);
    void save(Todo todo);
    void delete(Todo todo);
    void update(Todo todo);
}
