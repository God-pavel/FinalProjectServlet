package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.controller.validator.PasswordValidator;
import com.pasha.trainingcourse.controller.validator.Result;
import com.pasha.trainingcourse.controller.validator.UsernameValidator;
import com.pasha.trainingcourse.model.entity.enums.Role;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Objects;


public class RegistrationCommand implements Command {
    private static final Logger log = LogManager.getLogger();
    private UserService userService;

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!(Objects.nonNull(username) &&
                Objects.nonNull(password))) {
            return "/registration.jsp";
        }

        Result checkUsername = new UsernameValidator().validate(username);
        Result checkPassword = new PasswordValidator().validate(password);

        if (!checkUsername.isValid()) {
            request.setAttribute("message", checkUsername.getMessage());
            return "/registration.jsp";
        }
        if (!checkPassword.isValid()) {
            request.setAttribute("message", checkPassword.getMessage());
            return "/registration.jsp";
        }

        User user = new User(username,password, Collections.singleton(Role.USER));

        if (userService.registerUser(user)) {
            log.info("User successfully registered");
            return "redirect:/login";
        } else {
            log.warn("User can not be registered");
            request.setAttribute("message", "User with that username already exist!");
            return "/registration.jsp";
        }
    }
}