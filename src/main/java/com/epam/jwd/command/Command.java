package com.epam.jwd.command;

public interface Command {

    BaseCommandResponse execute(BaseCommandRequest request);

}