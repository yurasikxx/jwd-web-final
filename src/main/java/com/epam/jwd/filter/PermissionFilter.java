package com.epam.jwd.filter;

import com.epam.jwd.command.BaseApplicationCommand;
import com.epam.jwd.model.Role;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static com.epam.jwd.command.ApplicationCommand.COMMAND_PARAMETER_NAME;
import static com.epam.jwd.command.LogInCommand.PERSON_ROLE_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.model.Role.UNAUTHORIZED;

@WebFilter(urlPatterns = "/*")
public class PermissionFilter implements Filter {

    private static final String ERROR_PAGE_LOCATION = "/controller?command=error";

    private final Map<Role, Set<BaseApplicationCommand>> commandsByRoles;

    public PermissionFilter() {
        commandsByRoles = new EnumMap<>(Role.class);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);

        for (BaseApplicationCommand command : BaseApplicationCommand.values()) {
            for (Role allowedRole : command.getAllowedRoles()) {
                Set<BaseApplicationCommand> commands = commandsByRoles
                        .computeIfAbsent(allowedRole, k -> EnumSet.noneOf(BaseApplicationCommand.class));
                commands.add(command);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final BaseApplicationCommand command = BaseApplicationCommand.of(request.getParameter(COMMAND_PARAMETER_NAME));
        final HttpSession session = request.getSession(false);
        final Role currentRole = extractRoleFromSession(session);
        final Set<BaseApplicationCommand> allowedCommands = commandsByRoles.get(currentRole);
        if (allowedCommands.contains(command)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendRedirect(ERROR_PAGE_LOCATION);
        }
    }

    private Role extractRoleFromSession(HttpSession session) {
        return session != null && session.getAttribute(PERSON_ROLE_SESSION_ATTRIBUTE_NAME) != null
                ? (Role) session.getAttribute(PERSON_ROLE_SESSION_ATTRIBUTE_NAME)
                : UNAUTHORIZED;
    }

}