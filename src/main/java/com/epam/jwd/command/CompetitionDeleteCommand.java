package com.epam.jwd.command;

import com.epam.jwd.exception.DaoException;
import com.epam.jwd.exception.ServiceException;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import static com.epam.jwd.command.LogInCommand.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.EMPTY_ID_SENT_MSG;
import static com.epam.jwd.command.PersonDeleteCommand.ID_PARAMETER_NAME;
import static com.epam.jwd.command.PersonDeleteCommand.TRY_AGAIN_MSG;
import static com.epam.jwd.command.ShowCompetitionListPageCommand.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonDeletingPageCommand.DELETING_JSP_PATH;

public class CompetitionDeleteCommand implements Command {

    private static final String WRONG_ENTERED_DATA_MSG = "Competition with such ID doesn't exist or entered id is non-positive number";
    private static final String COMPETITION_CANNOT_BE_DELETED_MSG = "Competition cannot be deleted while there is unplayed bet";
    private static final String COMPETITION_SUCCESSFULLY_DELETED_MSG = "Competition successfully deleted";

    private static volatile CompetitionDeleteCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);
    private final BaseCommandResponse competitionErrorCommandResponse = new CommandResponse(DELETING_JSP_PATH, false);

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
        try {
            final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));

            if (!competitionService.canBeDeleted(id)) {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, WRONG_ENTERED_DATA_MSG);
                request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
                return competitionErrorCommandResponse;
            }

            competitionService.delete(id);
        } catch (ServiceException | DaoException e) {
            e.printStackTrace();
            request.setAttribute(ERROR_ATTRIBUTE_NAME, COMPETITION_CANNOT_BE_DELETED_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return competitionErrorCommandResponse;
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, EMPTY_ID_SENT_MSG);
            request.setAttribute(COMPETITION_ATTRIBUTE_NAME, TRY_AGAIN_MSG);
            return competitionErrorCommandResponse;
        }

        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, COMPETITION_SUCCESSFULLY_DELETED_MSG);

        return competitionCommandResponse;
    }

}