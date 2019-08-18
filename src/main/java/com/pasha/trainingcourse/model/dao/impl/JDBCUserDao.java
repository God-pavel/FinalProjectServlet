package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.dao.UserDao;
import com.pasha.trainingcourse.model.dao.mapper.UserMapper;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.entity.enums.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class JDBCUserDao implements UserDao {
    private Connection connection;
    private static final Logger log = LogManager.getLogger();
    private static final String INSERT_INTO_USER = "insert into user(username, password) values (?, ?)";
    private static final String SELECT_ID_FROM_USER = "select id from user where username=?";
    private static final String INSERT_INTO_USER_ROLES = "insert into user_roles(user_id, role) values (?, ?)";
    private static final String SELECT_FROM_USER_WHERE_USERNAME = "select * from user u left join user_roles ur on u.id = ur.user_id where username =?";
    private static final String SELECT_FROM_USER_WHERE_ID = "select * from user u left join user_roles ur on u.id = ur.user_id where id =?";
    private static final String SELECT_ALL_FROM_USER = "select * from user u left join user_roles ur on u.id = ur.user_id";
    private static final String UPDATE_USER = "update user set username=? where id=?";
    private static final String DELETE_FROM_USER = "delete from user_roles where user_id=?;";


    JDBCUserDao(Connection connection) {
        this.connection = connection;
        if (findByUsername("admin") == null) {
            create(new User("admin", "1", Set.of(Role.values())));
        }
    }

    @Override
    public void create(User entity) {

        try (PreparedStatement ps =
                     connection.prepareStatement(INSERT_INTO_USER)) {
            connection.setAutoCommit(false);
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.executeUpdate();
            insertRolesToDb(entity.getRoles(), entity.getUsername());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            rollBackTransaction(e, log, connection);
        }
    }

    private void insertRolesToDb(Set<Role> roles, String username) {
        try {
            PreparedStatement st = connection.prepareStatement(SELECT_ID_FROM_USER);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("id");
                PreparedStatement ps =
                        connection.prepareStatement(INSERT_INTO_USER_ROLES);
                for (Role role : roles) {
                    ps.setLong(1, id);
                    ps.setString(2, role.name());
                    ps.executeUpdate();
                }
            } else throw new SQLException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        UserMapper userMapper = new UserMapper();
        try (PreparedStatement ps =
                     connection.prepareStatement(SELECT_FROM_USER_WHERE_USERNAME)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return userMapper.extractFromRsWithALLRoles(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(Long id) {
        UserMapper userMapper = new UserMapper();
        try (PreparedStatement ps =
                     connection.prepareStatement(SELECT_FROM_USER_WHERE_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return userMapper.extractFromRsWithALLRoles(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<User> findAll() {

        Map<Long, User> users = new HashMap<>();

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT_ALL_FROM_USER);

            UserMapper userMapper = new UserMapper();

            while (rs.next()) {
                User user = userMapper
                        .extractFromResultSet(rs);
                userMapper.makeUnique(users, user, rs);
            }
            return new ArrayList<>(users.values());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(User entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement(UPDATE_USER)) {
            connection.setAutoCommit(false);
            ps.setString(1, entity.getUsername());
            ps.setLong(2, entity.getId());
            ps.executeUpdate();
            updateRoles(entity.getRoles(), entity.getUsername(), entity.getId());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            rollBackTransaction(e, log, connection);
        }
    }

    private void updateRoles(Set<Role> roles, String username, Long id) {
        try (PreparedStatement ps =
                     connection.prepareStatement(DELETE_FROM_USER)) {
            ps.setLong(1, id);
            ps.execute();
            insertRolesToDb(roles, username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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