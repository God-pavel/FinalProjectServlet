package com.pasha.trainingcourse.model.dao;

import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> extends AutoCloseable {
    void create(T entity);

    T findById(Long id);

    List<T> findAll();

    void update(T entity);

    void delete(Long id);

    void close();

    default void rollBack(Exception e, Logger log, Connection c) {
        log.error(e.getMessage());
        try {
            c.rollback();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
        throw new RuntimeException(e);
    }
}
