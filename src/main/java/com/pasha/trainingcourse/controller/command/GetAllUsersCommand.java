package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class GetAllUsersCommand implements Command {
    private UserService userService;

    GetAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("users", userService.getAllUsers());
        return "/all_users.jsp";
    }
}