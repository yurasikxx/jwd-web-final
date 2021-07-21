package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.List;

import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class CompetitionDeleteCommand implements Command {

    private static volatile CompetitionDeleteCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

    private CompetitionDeleteCommand() {
        this.competitionService = CompetitionService.getInstance();
    }

    public static CompetitionDeleteCommand getInstance() {
        if (instance == null) {
            synchronized (CompetitionDeleteCommand.class) {
                if (instance == null) {
                    instance = new CompetitionDeleteCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll();
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, competitions);
        final String id = request.getParameter(ID_PARAMETER_NAME);

        try {
            competitionService.delete(Long.parseLong(id));
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
        }

        return competitionCommandResponse;
    }

}