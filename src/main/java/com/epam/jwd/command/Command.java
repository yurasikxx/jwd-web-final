package com.epam.jwd.command;

public interface Command {

    CommandResponse execute(CommandRequest request);

}
