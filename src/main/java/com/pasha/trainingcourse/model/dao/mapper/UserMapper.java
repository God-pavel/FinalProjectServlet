package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.entity.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserMapper implements ObjectMapper<User> {

    @Override
    public User extractFromResultSet(ResultSet rs) throws SQLException {
        Set<Role> roles = new HashSet<>();
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        roles.add(Role.valueOf(rs.getString("role")));
        return new User(id, username, password, roles);
    }

    private void completeRoles(User user, ResultSet rs) throws SQLException {
        user.getRoles().add(Role.valueOf(rs.getString("role")));
    }

    public User extractFromRsWithALLRoles(ResultSet rs) throws SQLException {
        if (rs.next()) {
            User user = extractFromResultSet(rs);
            while (rs.next()) {
                completeRoles(user, rs);
            }
            return user;
        }
        return null;
    }

    @Override
    public void makeUnique(Map<Long, User> cache,
                           User user, ResultSet rs) throws SQLException {
        if (cache.keySet().contains(user.getId())) {

            cache.get(user.getId()).getRoles().add(Role.valueOf(rs.getString("role")));
        } else {
            cache.put(user.getId(), user);
        }

    }

}
