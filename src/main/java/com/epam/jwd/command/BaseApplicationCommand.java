package com.epam.jwd.command;

import com.epam.jwd.model.Role;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.model.Role.ADMINISTRATOR;
import static com.epam.jwd.model.Role.UNAUTHORIZED;
import static com.epam.jwd.model.Role.USER;

public enum BaseApplicationCommand {

    MAIN_PAGE(ShowMainPageCommand.getInstance()),
    REGISTER_PAGE(ShowRegisterPageCommand.getInstance(), UNAUTHORIZED),
    REGISTER(RegisterCommand.getInstance(), UNAUTHORIZED),
    LOG_IN_PAGE(ShowLogInPageCommand.getInstance(), UNAUTHORIZED),
    LOG_IN(LogInCommand.getInstance(), UNAUTHORIZED),
    LOG_OUT(LogOutCommand.getInstance(), ADMINISTRATOR, USER),
    PERSON_MANAGEMENT_PAGE(ShowPersonManagementPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_LIST_PAGE(ShowPersonListPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_ADDING_PAGE(ShowPersonAddingPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_ADD(PersonAddingCommand.getInstance(), ADMINISTRATOR),
    PERSON_DELETING_PAGE(ShowPersonDeletingPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_DELETE(PersonDeleteCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_MANAGEMENT_PAGE(ShowCompetitionManagementPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_LIST_PAGE(ShowCompetitionListPageCommand.getInstance()),
    COMPETITION_ADDING_PAGE(ShowCompetitionAddingPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_ADD(CompetitionAddingCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_DELETING_PAGE(ShowCompetitionDeletingPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_DELETE(CompetitionDeleteCommand.getInstance(), ADMINISTRATOR),
    BETSLIP_MANAGEMENT_PAGE(ShowBetslipManagementPageCommand.getInstance(), ADMINISTRATOR),
    BETSLIP_LIST_PAGE(ShowBetslipListPageCommand.getInstance(), ADMINISTRATOR, USER),
    BETSLIP_ADDING_PAGE(ShowBetslipAddingPageCommand.getInstance(), ADMINISTRATOR),
    BETSLIP_ADD(BetslipAddingCommand.getInstance(), ADMINISTRATOR),
    BETSLIP_DELETING_PAGE(ShowBetslipDeletingPageCommand.getInstance(), ADMINISTRATOR),
    BETSLIP_DELETE(BetslipDeleteCommand.getInstance(), ADMINISTRATOR),
    BET_MANAGEMENT_PAGE(ShowBetManagementPageCommand.getInstance(), ADMINISTRATOR),
    BET_LIST_PAGE(ShowBetListPageCommand.getInstance(), ADMINISTRATOR),
    BET_ADDING_PAGE(ShowBetAddingPageCommand.getInstance(), ADMINISTRATOR),
    BET_ADD(BetAddingCommand.getInstance(), ADMINISTRATOR),
    BET_DELETING_PAGE(ShowBetDeletingPageCommand.getInstance(), ADMINISTRATOR),
    BET_DELETE(BetDeleteCommand.getInstance(), ADMINISTRATOR),
    ERROR(ShowErrorPageCommand.getInstance()),
    DEFAULT(ShowMainPageCommand.getInstance());

    private final Command command;
    private final List<Role> allowedRoles;

    BaseApplicationCommand(Command command, Role... roles) {
        this.command = command;
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : Role.valuesAsList();
    }

    public Command getCommand() {
        return command;
    }

    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    public static BaseApplicationCommand of(String name) {
        for (BaseApplicationCommand command : values()) {
            if (command.name().equalsIgnoreCase(name)) {
                return command;
            }
        }

        return DEFAULT;
    }

}