package com.pasha.trainingcourse.model.service;

import com.pasha.trainingcourse.model.dao.DaoFactory;
import com.pasha.trainingcourse.model.dao.UserDao;
import com.pasha.trainingcourse.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger log = LogManager.getLogger();
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public List<User> getAllUsers(){
        try (UserDao dao = daoFactory.createUserDao()){
            return dao.findAll();
        } catch (Exception e){
            return Collections.emptyList();
        }
    }

    public boolean registerUser(User user){

        try(UserDao dao = daoFactory.createUserDao()){
            User userFromDb = dao.findByUsername(user.getUsername());
            if (userFromDb != null) {
                log.warn("login not unique!");
                return false;
            }
            log.info("before creating");
            dao.create(user);
            return true;
        } catch (Exception e){
            log.warn(e.getMessage());
            e.getStackTrace();
            return false;
        }
    }

    public User getUserById(Long id) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

}
