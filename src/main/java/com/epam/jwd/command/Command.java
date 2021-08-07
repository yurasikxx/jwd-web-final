package com.epam.jwd.command;

/**
 * A {@code Command} interface contains only method that executes a specific command.
 */
public interface Command {

    /**
     * Accepts command request and executes given command.
     *
     * @param request a given command request.
     * @return a command response.
     */
    BaseCommandResponse execute(BaseCommandRequest request);

}