package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.Role;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
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

        User user = new User(username,password,Role.USER);


        log.info("User to be registered: " + user);

        if (userService.registerUser(user)) {
            log.info("User successfully registered");
            return "redirect:/login";
        } else {
            log.info("User can not be registered");
            request.setAttribute("error", true);
            return "/registration.jsp";
        }
    }
}