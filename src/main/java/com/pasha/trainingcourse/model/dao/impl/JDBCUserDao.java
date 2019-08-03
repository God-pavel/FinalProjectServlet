package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.Role;
import com.pasha.trainingcourse.model.dao.UserDao;
import com.pasha.trainingcourse.model.dao.mapper.UserMapper;
import com.pasha.trainingcourse.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUserDao implements UserDao {
    private Connection connection;
    private static final Logger log = LogManager.getLogger();


    public JDBCUserDao(Connection connection) {
        log.info("try to get connection at JDBCUserDao");
        this.connection = connection;
        log.info("got connection at JDBCUserDao");
        log.info(connection);
        if(findByUsername("admin")==null) {
            create(new User("admin", "1", Role.ADMIN));
        }
    }

    @Override
    public void create(User entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement("insert into user(username, password, role) values (?, ?, ?)")) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getRole().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User findByUsername(String username){
        UserMapper userMapper = new UserMapper();
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from user where username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return userMapper.extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User findById(Long id) {
        return null;
    }


    @Override
    public List<User> findAll() {

        Map<Long, User> users = new HashMap<>();

        final String query = " select * from user";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);


            UserMapper userMapper = new UserMapper();

            while (rs.next()) {
                User user = userMapper
                        .extractFromResultSet(rs);

                user = userMapper
                        .makeUnique(users, user);

            }
            return new ArrayList<>(users.values());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}