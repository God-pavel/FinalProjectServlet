package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.exception.ZReportAlreadyCreatedException;
import com.pasha.trainingcourse.model.service.CheckService;
import com.pasha.trainingcourse.model.service.ReportService;
import com.pasha.trainingcourse.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CreateCheckCommand implements Command {
    private CheckService checkService;
    private ReportService reportService;

    CreateCheckCommand(CheckService checkService, ReportService reportService) {
        this.checkService = checkService;
        this.reportService = reportService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        if (reportService.getTodayZReport()) {
            return "redirect:/main?message=Can't create new check today";
        }
        String mes = request.getParameter("message");
        if(mes!=null){
            request.setAttribute("message",mes);
        }
        String checkID = request.getRequestURI().replaceAll(".*/createCheck", "");
        if (checkID.equals("")) {
            HttpSession session = request.getSession();
            Check check = checkService.createTempCheck((User) session.getAttribute("user"));
            request.setAttribute("check", check);
            return "/check_form.jsp";
        } else {
            checkID = checkID.replaceAll("/", "");
            Check check = checkService.getTemporaryCheckById(Long.parseLong(checkID));
            request.setAttribute("check", check);
            return "/check_form.jsp";
        }

    }

}
