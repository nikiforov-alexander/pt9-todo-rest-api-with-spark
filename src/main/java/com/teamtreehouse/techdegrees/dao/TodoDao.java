package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {
    List<Todo> findAll();
    Todo findById(int id);
    int save(Todo todo) throws DaoException;
    void delete(Todo todo);
    void update(Todo todo);
}
