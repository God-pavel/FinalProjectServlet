package com.pasha.trainingcourse.controller;

import com.pasha.trainingcourse.controller.command.Command;
import com.pasha.trainingcourse.controller.command.CommandManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet extends HttpServlet {

    private CommandManager commandManager = CommandManager.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("doPost");
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("doGet");
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        String path = request.getRequestURI().replaceAll(".*/app", "");
        Command command = commandManager.getCommand(path);
        System.out.println("path: " + path +", command: "+command);

        String page = command.execute(request);

        if (page.contains("redirect")) {
            response.sendRedirect(request.getContextPath() +
                    request.getServletPath() +
                    page.replace("redirect:", ""));
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}