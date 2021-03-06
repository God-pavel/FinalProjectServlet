package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.stream.Collectors;

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
            return "/WEB-INF/pages/login.jsp";
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            request.setAttribute("message", true);
            return "/WEB-INF/pages/login.jsp";
        }


        if (user.getPassword().equals(DigestUtils.md5Hex(password))) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            Set<String> userRoles = user.getRoles().stream().map(Enum::name)
                    .collect(Collectors.toSet());
            session.setAttribute("userRoles", userRoles);
            return "redirect:/index";
        } else {
            request.setAttribute("message", true);
            return "/WEB-INF/pages/login.jsp";
        }
    }
}