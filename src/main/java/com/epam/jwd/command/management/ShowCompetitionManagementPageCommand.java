package com.epam.jwd.command.management;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;

/**
 * A {@code ShowCompetitionManagementPageCommand} class implements {@code Command}
 * interface and execute command that showing person competition page.
 *
 * @see Command
 */
public class ShowCompetitionManagementPageCommand implements Command {

    private static final String COMPETITION_JSP_PATH = "/jsp/competition.jsp";

    private static volatile ShowCompetitionManagementPageCommand instance;

    private final BaseCommandResponse competitionCommandResponse = new CommandResponse(COMPETITION_JSP_PATH, false);

    private ShowCompetitionManagementPageCommand() {
    }

    public static ShowCompetitionManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return competitionCommandResponse;
    }

}