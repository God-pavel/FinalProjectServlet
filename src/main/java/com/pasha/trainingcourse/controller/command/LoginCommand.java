package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.Role;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class LoginCommand implements Command {

    private UserService userService;

    LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null) {
            return "/login.jsp";
        }

        User user = userService.getUserByUsername(username);
        if (user==null) {
            request.setAttribute("message", true);
            return "/login.jsp";
        }



        if (user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            Set<Role> allRoles = new HashSet<>();
            allRoles.add(Role.USER);
            allRoles.add(Role.ADMIN);
            allRoles.add(Role.MERCHANDISER);
            allRoles.add(Role.SENIOR_CASHIER);
            session.setAttribute("roles", allRoles);
            return "redirect:/index";
        } else {
            request.setAttribute("message", true);
            return "/login.jsp";
        }
    }
}