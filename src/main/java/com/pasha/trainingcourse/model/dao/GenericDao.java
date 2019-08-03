package com.pasha.trainingcourse.model.dao;

import java.util.List;

public interface GenericDao<T> extends AutoCloseable {
    void create (T entity);
    T findById(Long id);
    List<T> findAll();
    void update(T entity);
    void delete(Long id);
    void close();
}
