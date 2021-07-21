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
    PERSON_DELETE_PAGE(ShowPersonDeletingPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_DELETE(PersonDeleteCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_PAGE(ShowCompetitionPageCommand.getInstance()),
    BETSLIP_PAGE(ShowBetslipPageCommand.getInstance(), ADMINISTRATOR, USER),
    BET_PAGE(ShowBetPageCommand.getInstance(), ADMINISTRATOR),
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