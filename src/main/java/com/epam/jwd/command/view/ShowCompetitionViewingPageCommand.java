package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.AbstractBaseEntity;
import com.epam.jwd.model.Competition;
import com.epam.jwd.service.CompetitionBaseService;
import com.epam.jwd.service.CompetitionService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.jwd.constant.Constant.COMPETITION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

/**
 * A {@code ShowCompetitionViewingPageCommand} class implements {@code Command}
 * interface and execute command that showing competition viewing page.
 *
 * @see Command
 */
public class ShowCompetitionViewingPageCommand implements Command {

    private static volatile ShowCompetitionViewingPageCommand instance;

    private final CompetitionBaseService competitionService;
    private final BaseCommandResponse competitionPageResponse;

    private ShowCompetitionViewingPageCommand() {
        this.competitionService = CompetitionService.getInstance();
        this.competitionPageResponse = new CommandResponse(VIEWING_JSP_PATH, false);
    }

    public static ShowCompetitionViewingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowCompetitionViewingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowCompetitionViewingPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final List<Competition> competitions = competitionService.findAll()
                .stream()
                .sorted(Comparator.comparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());
        request.setAttribute(COMPETITION_ATTRIBUTE_NAME, competitions);

        return competitionPageResponse;
    }

}