package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.Role;
import com.pasha.trainingcourse.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserMapper implements ObjectMapper<User> {

    @Override
    public User extractFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role")));
    }

    @Override
    public User makeUnique(Map<Long, User> cache,
                           User user) {
        cache.putIfAbsent(user.getId(), user);
        return cache.get(user.getId());
    }

}
