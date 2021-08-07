package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.constant.Constant.BET_HISTORY_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.CHANGING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;

/**
 * A {@code ShowCompetitionResultsCommittingPageCommand} class implements {@code Command}
 * interface and execute command that showing competition results committing page.
 *
 * @see Command
 */
public class ShowCompetitionResultsCommittingPageCommand implements Command {

    private static final String COMPETITION_RESULTS_COMMITTING_MESSAGE_KEY = "competition.results.committing";

    private static volatile ShowCompetitionResultsCommittingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse betHistoryCommandResponse;

    private ShowCompetitionResultsCommittingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.betHistoryCommandResponse = new CommandResponse(CHANGING_JSP_PATH, false);
        this.competitionService = CompetitionService.getInstance();
    }

    public static ShowCompetitionResultsCommittingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionResultsCommittingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionResultsCommittingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll();

        request.setAttribute(BET_HISTORY_ATTRIBUTE_NAME, messageManager.getString(COMPETITION_RESULTS_COMMITTING_MESSAGE_KEY));
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);

        return betHistoryCommandResponse;
    }

}