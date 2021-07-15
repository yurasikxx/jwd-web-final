package com.epam.jwd.controller;

import com.epam.jwd.command.ApplicationCommand;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandRequest;
import com.epam.jwd.command.CommandResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/controller")
public class ApplicationController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final Command command = ApplicationCommand.getInstance().resolveCommand(req);
        final CommandResponse response = command.execute(new CommandRequest() {
            @Override
            public void setAttribute(String name, Object value) {
                req.setAttribute(name, value);
            }
        });

        try {
            if (response.isRedirect()) {
                resp.sendRedirect(response.getPath());
            } else {
                final RequestDispatcher dispatcher = req.getRequestDispatcher(response.getPath());
                dispatcher.forward(req, resp);
            }
        } catch (IOException | ServletException e) {
            LOGGER.error(e.getMessage());
        }
    }

}