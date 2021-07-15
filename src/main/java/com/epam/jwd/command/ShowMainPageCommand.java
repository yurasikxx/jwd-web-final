package com.epam.jwd.command;

public class ShowMainPageCommand implements Command {

    private static volatile ShowMainPageCommand instance;

    private ShowMainPageCommand() {
    }

    public static ShowMainPageCommand getInstance() {
        if (instance == null) {
            synchronized (ShowMainPageCommand.class) {
                if (instance == null) {
                    instance = new ShowMainPageCommand();
                }
            }
        }

        return instance;
    }

    private static final CommandResponse SHOW_MAIN_PAGE = new CommandResponse() {
        @Override
        public String getPath() {
            return "/WEB-INF/jsp/main.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public CommandResponse execute(CommandRequest request) {
        return SHOW_MAIN_PAGE;
    }

}