package com.epam.jwd.command;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public interface BaseCommandRequest {

    HttpSession createSession();

    Optional<HttpSession> getCurrentSession();

    void invalidateCurrentSession();

    String getParameter(String name);

    void setAttribute(String name, Object value);

}