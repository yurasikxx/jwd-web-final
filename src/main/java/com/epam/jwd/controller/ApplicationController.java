package com.epam.jwd.controller;

import com.epam.jwd.command.ApplicationCommand;
import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandRequest;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.PersonBaseService;
import com.epam.jwd.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static com.epam.jwd.constant.Constant.LOCALE_PARAMETER_NAME;

/**
 * An {@code ApplicationController} class is a main application servlet
 * that performs controller function by managing command executing.
 *
 * @see Command
 */
@WebServlet("/controller")
public class ApplicationController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationController.class);

    private static final String PERSONS_WERE_INITIALIZED_MSG = "Persons were initialized";
    private static final String PERSONS_WERE_NOT_INITIALIZED_MSG = "Persons weren't initialized";
    private static final String PERSONS_WERE_DESTROYED_MSG = "Persons were destroyed";
    private static final String PERSONS_WERE_NOT_DESTROYED_MSG = "Persons weren't destroyed";

    private final PersonBaseService personService = PersonService.getInstance();

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            personService.init();
            LOGGER.info(PERSONS_WERE_INITIALIZED_MSG);
        } catch (ServiceException e) {
            LOGGER.error(PERSONS_WERE_NOT_INITIALIZED_MSG);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        process(req, resp);
    }

    @Override
    public void destroy() {
        try {
            personService.destroy();
            LOGGER.info(PERSONS_WERE_DESTROYED_MSG);
        } catch (ServiceException e) {
            LOGGER.error(PERSONS_WERE_NOT_DESTROYED_MSG);
        }

        super.destroy();
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();

        if (session.getAttribute(LOCALE_PARAMETER_NAME) == null) {
            session.setAttribute(LOCALE_PARAMETER_NAME, Locale.getDefault());
        }

        final Command command = ApplicationCommand.getInstance().resolveCommand(req);
        final BaseCommandRequest request = new CommandRequest(req);
        final BaseCommandResponse response = command.execute(request);

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