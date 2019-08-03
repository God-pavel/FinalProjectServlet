package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class UserEditCommand implements Command {
    private UserService userService;

    UserEditCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String username = request.getParameter("username");
        String[] roles = request.getParameterValues("roles");
        System.out.println("Got username: " + username+ ", roles: " + roles);
        return "/user_edit.jsp";
    }
}