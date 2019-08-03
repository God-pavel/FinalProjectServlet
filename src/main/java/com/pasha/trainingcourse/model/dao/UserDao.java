package com.pasha.trainingcourse.model.dao;

import com.pasha.trainingcourse.model.entity.User;

public interface UserDao extends GenericDao<User> {
    User findByUsername(String username);
}
