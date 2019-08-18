package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.CheckService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MainCommandTest {
    private MainCommand mainCommand;
    private CheckService checkService;

    @Before
    public void init() {
        checkService = mock(CheckService.class);
        mainCommand = new MainCommand(checkService);
    }

    @Test
    public void execute() {

        doReturn(0L)
                .when(checkService)
                .getNumberOfChecks();
        doReturn(new ArrayList<>())
                .when(checkService)
                .getAllChecks();

        HttpServletRequest request = mock(HttpServletRequest.class);

        String path = mainCommand.execute(request);

        assertEquals("/WEB-INF/pages/main.jsp", path);
    }


}
