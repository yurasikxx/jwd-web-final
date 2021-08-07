package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.BetHistory;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.BetHistoryBaseService;
import com.epam.jwd.service.BetHistoryService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

/**
 * A {@code ShowPersonBetHistoryPageCommand} class implements {@code Command}
 * interface and execute command that showing bet history viewing page.
 *
 * @see Command
 */
public class ShowPersonBetHistoryPageCommand implements Command {

    private static final String PERSON_BET_HISTORY_ATTRIBUTE_NAME = "personBetHistory";

    private static volatile ShowPersonBetHistoryPageCommand instance;

    private final BetHistoryBaseService betHistoryService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowPersonBetHistoryPageCommand() {
        this.betHistoryService = BetHistoryService.getInstance();
    }

    public static ShowPersonBetHistoryPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonBetHistoryPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonBetHistoryPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        final Optional<HttpSession> session = request.getCurrentSession();
        HttpSession currentSession = null;
        if (session.isPresent()) {
            currentSession = session.get();
        }
        final String currentPersonLogin = extractPersonNameFromSession(currentSession);

        final List<BetHistory> bets = betHistoryService.findAll();
        final List<BetHistory> currentUserBets = new ArrayList<>();

        for (BetHistory betHistory : bets) {
            if (betHistory.getPersonLogin().equals(currentPersonLogin)) {
                currentUserBets.add(betHistory);
            }
        }

        request.setAttribute(PERSON_BET_HISTORY_ATTRIBUTE_NAME, currentUserBets);

        return betslipCommandResponse;
    }

    private String extractPersonNameFromSession(HttpSession session) {
        return session != null && session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME) != null
                ? (String) session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME)
                : Role.UNAUTHORIZED.getName();
    }

}