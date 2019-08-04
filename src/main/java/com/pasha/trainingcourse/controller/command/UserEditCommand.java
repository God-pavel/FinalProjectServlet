package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.enums.Role;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserEditCommand implements Command {
    private UserService userService;

    UserEditCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        Set<String> allRoles = new HashSet<>();
        Arrays.stream(Role.values()).forEach(role->allRoles.add(role.name()));
        request.setAttribute("allRoles", allRoles);

        String userId = request.getRequestURI().replaceAll(".*/userEdit/", "");
        User user = userService.getUserById(Long.parseLong(userId));
        request.setAttribute("user", user);

        String username = request.getParameter("username");
        String[] roleNames = request.getParameterValues("roles");
        if(roleNames == null){
            return "/user_edit.jsp";
        }
        Set<Role> roles = Arrays.stream(roleNames)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        user.setUsername(username);
        user.setRoles(roles);
        if(!userService.updateUser(user)){
            request.setAttribute("error", true);
            return "/user_edit.jsp";
        }

        return "redirect:/users";
    }
}