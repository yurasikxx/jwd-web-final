package com.epam.jwd.command.view;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.model.Bet;
import com.epam.jwd.model.Role;
import com.epam.jwd.service.BetBaseService;
import com.epam.jwd.service.BetService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.VIEWING_JSP_PATH;

public class ShowPersonBetsViewingPageCommand implements Command {

    private static final String PERSON_BET_ATTRIBUTE_NAME = "personBet";

    private static volatile ShowPersonBetsViewingPageCommand instance;

    private final BetBaseService betService;
    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(VIEWING_JSP_PATH, false);

    private ShowPersonBetsViewingPageCommand() {
        this.betService = BetService.getInstance();
    }

    public static ShowPersonBetsViewingPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowPersonBetsViewingPageCommand.class) {
                if (instance == null) {
                    instance = new ShowPersonBetsViewingPageCommand();
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