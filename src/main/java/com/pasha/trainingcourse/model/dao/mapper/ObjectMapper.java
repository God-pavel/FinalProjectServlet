package com.pasha.trainingcourse.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface ObjectMapper<T> {

    T extractFromResultSet(ResultSet rs) throws SQLException;

    void makeUnique(Map<Long, T> cache,
                 T entity, ResultSet rs) throws  SQLException;
}
