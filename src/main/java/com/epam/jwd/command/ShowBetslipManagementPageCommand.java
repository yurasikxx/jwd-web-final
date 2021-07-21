package com.epam.jwd.command;

public class ShowBetslipManagementPageCommand implements Command {

    private static final String BETSLIP_JSP_PATH = "/WEB-INF/jsp/betslip.jsp";

    private static volatile ShowBetslipManagementPageCommand instance;

    private final BaseCommandResponse betslipCommandResponse = new CommandResponse(BETSLIP_JSP_PATH, false);

    private ShowBetslipManagementPageCommand() {
    }

    public static ShowBetslipManagementPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowBetslipManagementPageCommand.class) {
                if (instance == null) {
                    instance = new ShowBetslipManagementPageCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        return betslipCommandResponse;
    }

}