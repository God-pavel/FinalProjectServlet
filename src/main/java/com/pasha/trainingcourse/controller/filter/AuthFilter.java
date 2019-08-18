package com.pasha.trainingcourse.controller.filter;


import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.entity.enums.Role;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class AuthFilter implements Filter {

    private final List<String> adminPaths = Arrays.asList("/index", "/logout", "/users", "/userEdit");
    private final List<String> userPaths = Arrays.asList("/index", "/main", "/createCheck", "/addByName", "/addById", "/closeCheck", "/storage", "/logout");
    private final List<String> merchPaths = Arrays.asList("/index", "/merchandise", "/storage", "/logout", "/productEdit");
    private final List<String> seniorPaths = Arrays.asList("/index", "/main", "/createCheck", "/addByName", "/addById", "/closeCheck", "/deleteCheck", "/deleteProduct", "/createXReport", "/createZReport", "/report", "/storage", "/logout");
    private final List<String> defaultPaths = Arrays.asList("/index", "/login", "/registration", "/back.jpg");
    private Map<Role, List<String>> allowedPathPatterns = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowedPathPatterns.put(Role.USER, userPaths);
        allowedPathPatterns.put(Role.ADMIN, adminPaths);
        allowedPathPatterns.put(Role.MERCHANDISER, merchPaths);
        allowedPathPatterns.put(Role.SENIOR_CASHIER, seniorPaths);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI().replaceAll(".*/app", "");

        User user = (User) session.getAttribute("user");

        if (Objects.isNull(user)) {
            if (defaultPaths.contains(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            } else {
                response.sendRedirect(request.getContextPath() +
                        request.getServletPath() +
                        "/login");
                return;
            }
        }
        List<String> paths = new ArrayList<>();

        for (Role role : user.getRoles()) {
            paths.addAll(allowedPathPatterns.get(role));
        }
        boolean contains = false;
        for (String key : paths) {
            if (requestURI.contains(key)) {
                contains = true;
                filterChain.doFilter(request, response);
                break;
            }
        }
        if (!contains) {
            response.setStatus(403);
            response.sendRedirect("/forbidden.jsp");
        }
    }


    @Override
    public void destroy() {

    }
}