package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.TodoTask;

import java.util.List;

public interface TodoDao {
    List<TodoTask> findAll();
    TodoTask findById(int id);
    int save(TodoTask todoTask) throws DaoException;
    void delete(TodoTask todoTask) throws DaoException;
    void update(TodoTask todoTask) throws DaoException;
}
