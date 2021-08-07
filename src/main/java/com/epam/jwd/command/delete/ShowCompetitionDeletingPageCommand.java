package com.epam.jwd.command.delete;

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

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.DELETING_JSP_PATH;
import static com.epam.jwd.constant.Constant.SELECT_COMPETITION_ATTRIBUTE_NAME;

/**
 * A {@code ShowCompetitionDeletingPageCommand} class implements {@code Command}
 * interface and execute command that showing competition deleting page.
 *
 * @see Command
 */
public class ShowCompetitionDeletingPageCommand implements Command {

    private static final String COMPETITION_DELETING_MESSAGE_KEY = "competition.deleting";

    private static volatile ShowCompetitionDeletingPageCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionDeletingPageResponse;

    private ShowCompetitionDeletingPageCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.competitionService = CompetitionService.getInstance();
        this.competitionDeletingPageResponse = new CommandResponse(DELETING_JSP_PATH, false);
    }

    public static ShowCompetitionDeletingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionDeletingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionDeletingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll();

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME,
                messageManager.getString(COMPETITION_DELETING_MESSAGE_KEY));
        request.setAttribute(SELECT_COMPETITION_ATTRIBUTE_NAME, competitions);

        return competitionDeletingPageResponse;
    }

}