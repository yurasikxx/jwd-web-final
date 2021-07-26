package com.epam.jwd.command;

import com.epam.jwd.model.Bet;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.jwd.command.LogInCommand.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.command.ShowPersonListPageCommand.LIST_JSP_PATH;

public class ShowPersonBetsListPageCommand implements Command {

    private static final String PERSON_BET_ATTRIBUTE_NAME = "personBet";

    private static volatile ShowPersonBetsListPageCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(LIST_JSP_PATH, false);

    private ShowPersonBetsListPageCommand() {
        this.betService = BetService.getInstance();
    }

    public static ShowPersonBetsListPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonBetsListPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonBetsListPageCommand();
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

        final List<Bet> bets = betService.findAll();
        final List<Bet> currentUserBets = new ArrayList<>();

        for (Bet bet : bets) {
            if (bet.getPerson().getLogin().equals(currentPersonLogin)) {
                currentUserBets.add(bet);
            }
        }

        request.setAttribute(PERSON_BET_ATTRIBUTE_NAME, currentUserBets);

        return betslipCommandResponse;
    }

    private String extractPersonNameFromSession(HttpSession session) {
        return session != null && session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME) != null
                ? (String) session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME)
                : Role.UNAUTHORIZED.getName();
    }

}