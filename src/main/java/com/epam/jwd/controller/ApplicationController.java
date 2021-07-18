package com.epam.jwd.controller;

import com.epam.jwd.command.ApplicationCommand;
import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandRequest;
import com.epam.jwd.dao.PersonBaseDao;
import com.epam.jwd.dao.PersonDao;
import com.epam.jwd.exception.DaoException;
import com.epam.jwd.model.Person;
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
import java.io.IOException;
import java.util.List;

@WebServlet("/controller")
public class ApplicationController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationController.class);
    private final PersonBaseService personService = PersonService.getInstance();
    private final PersonBaseDao personDao = PersonDao.getInstance();
    private final List<Person> persons = personService.findAll();

    @Override
    public void init() throws ServletException {
        super.init();

        for (int i = 0; i < personService.findAll().size(); i++) {
            try {
                personService.update(personService.findAll().get(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
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
        for (int i = 0; i < personDao.findAll().size(); i++) {
            try {
                personDao.update(persons.get(i));
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

        super.destroy();
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
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