package com.epam.jwd.command;

import com.epam.jwd.command.add.ParlayBetAddingCommand;
import com.epam.jwd.command.add.ShowParlayBetAddingPageCommand;
import com.epam.jwd.command.add.ShowSystemBetAddingPageCommand;
import com.epam.jwd.command.add.SingleBetAddingCommand;
import com.epam.jwd.command.add.BetslipAddingCommand;
import com.epam.jwd.command.add.CompetitionAddingCommand;
import com.epam.jwd.command.add.PersonAddingCommand;
import com.epam.jwd.command.add.RegisterCommand;
import com.epam.jwd.command.add.ShowSingleBetAddingPageCommand;
import com.epam.jwd.command.add.ShowBetslipAddingPageCommand;
import com.epam.jwd.command.add.ShowCompetitionAddingPageCommand;
import com.epam.jwd.command.add.ShowPersonAddingPageCommand;
import com.epam.jwd.command.add.ShowRegistrationPageCommand;
import com.epam.jwd.command.add.SystemBetAddingCommand;
import com.epam.jwd.command.auth.LogInCommand;
import com.epam.jwd.command.auth.LogOutCommand;
import com.epam.jwd.command.auth.ShowLogInPageCommand;
import com.epam.jwd.command.change.BetslipChangingCommand;
import com.epam.jwd.command.change.CompetitionChangingCommand;
import com.epam.jwd.command.change.CompetitionResultsCommitCommand;
import com.epam.jwd.command.change.LanguageSelectionCommand;
import com.epam.jwd.command.change.PasswordChangingCommand;
import com.epam.jwd.command.change.PersonChangingCommand;
import com.epam.jwd.command.change.ShowBetslipChangingPageCommand;
import com.epam.jwd.command.change.ShowCompetitionChangingPageCommand;
import com.epam.jwd.command.change.ShowPasswordChangingCommand;
import com.epam.jwd.command.change.ShowPersonChangingPageCommand;
import com.epam.jwd.command.delete.BetslipDeletingCommand;
import com.epam.jwd.command.delete.CompetitionDeletingCommand;
import com.epam.jwd.command.delete.PersonDeletingCommand;
import com.epam.jwd.command.delete.ShowBetslipDeletingPageCommand;
import com.epam.jwd.command.delete.ShowCompetitionDeletingPageCommand;
import com.epam.jwd.command.delete.ShowPersonDeletingPageCommand;
import com.epam.jwd.command.management.ShowBetManagementPageCommand;
import com.epam.jwd.command.management.ShowBetslipManagementPageCommand;
import com.epam.jwd.command.management.ShowCompetitionManagementPageCommand;
import com.epam.jwd.command.management.ShowPersonManagementPageCommand;
import com.epam.jwd.command.page.ShowErrorPageCommand;
import com.epam.jwd.command.page.ShowMainPageCommand;
import com.epam.jwd.command.page.ShowSuccessPageCommand;
import com.epam.jwd.command.view.ShowAllBetsViewingPageCommand;
import com.epam.jwd.command.view.ShowBetHistoryPageCommand;
import com.epam.jwd.command.view.ShowBetslipViewingPageCommand;
import com.epam.jwd.command.view.ShowCompetitionViewingPageCommand;
import com.epam.jwd.command.view.ShowPersonBetHistoryPageCommand;
import com.epam.jwd.command.view.ShowPersonBetsViewingPageCommand;
import com.epam.jwd.command.view.ShowPersonViewingPageCommand;
import com.epam.jwd.model.Role;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.model.Role.ADMINISTRATOR;
import static com.epam.jwd.model.Role.BOOKMAKER;
import static com.epam.jwd.model.Role.UNAUTHORIZED;
import static com.epam.jwd.model.Role.USER;

/**
 * A {@code BaseApplicationCommand} enum store all application commands
 *
 * @see ApplicationCommand
 */
public enum BaseApplicationCommand {

    MAIN_PAGE(ShowMainPageCommand.getInstance()),
    REGISTER_PAGE(ShowRegistrationPageCommand.getInstance(), UNAUTHORIZED),
    REGISTER(RegisterCommand.getInstance(), UNAUTHORIZED),
    LOG_IN_PAGE(ShowLogInPageCommand.getInstance(), UNAUTHORIZED),
    LOG_IN(LogInCommand.getInstance(), UNAUTHORIZED),
    LOG_OUT(LogOutCommand.getInstance(), ADMINISTRATOR, BOOKMAKER, USER),
    PERSON_MANAGEMENT_PAGE(ShowPersonManagementPageCommand.getInstance(), ADMINISTRATOR),
    PASSWORD_CHANGING_PAGE(ShowPasswordChangingCommand.getInstance(), USER),
    PASSWORD_CHANGE(PasswordChangingCommand.getInstance(), USER),
    PERSON_LIST_PAGE(ShowPersonViewingPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_ADDING_PAGE(ShowPersonAddingPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_ADD(PersonAddingCommand.getInstance(), ADMINISTRATOR),
    PERSON_CHANGING_PAGE(ShowPersonChangingPageCommand.getInstance(), ADMINISTRATOR, USER),
    PERSON_CHANGE(PersonChangingCommand.getInstance(), ADMINISTRATOR, USER),
    PERSON_DELETING_PAGE(ShowPersonDeletingPageCommand.getInstance(), ADMINISTRATOR),
    PERSON_DELETE(PersonDeletingCommand.getInstance(), ADMINISTRATOR),
    PERSON_BETS_LIST_PAGE(ShowPersonBetsViewingPageCommand.getInstance(), USER),
    PERSON_BET_HISTORY_PAGE(ShowPersonBetHistoryPageCommand.getInstance(), USER),
    COMPETITION_MANAGEMENT_PAGE(ShowCompetitionManagementPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_LIST_PAGE(ShowCompetitionViewingPageCommand.getInstance(), ADMINISTRATOR, BOOKMAKER, USER),
    COMPETITION_ADDING_PAGE(ShowCompetitionAddingPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_ADD(CompetitionAddingCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_CHANGING_PAGE(ShowCompetitionChangingPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_CHANGE(CompetitionChangingCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_DELETING_PAGE(ShowCompetitionDeletingPageCommand.getInstance(), ADMINISTRATOR),
    COMPETITION_DELETE(CompetitionDeletingCommand.getInstance(), ADMINISTRATOR),
    BETSLIP_MANAGEMENT_PAGE(ShowBetslipManagementPageCommand.getInstance(), BOOKMAKER),
    BETSLIP_LIST_PAGE(ShowBetslipViewingPageCommand.getInstance(), ADMINISTRATOR, BOOKMAKER, USER),
    BETSLIP_ADDING_PAGE(ShowBetslipAddingPageCommand.getInstance(), BOOKMAKER),
    BETSLIP_ADD(BetslipAddingCommand.getInstance(), BOOKMAKER),
    BETSLIP_CHANGING_PAGE(ShowBetslipChangingPageCommand.getInstance(), BOOKMAKER),
    BETSLIP_CHANGE(BetslipChangingCommand.getInstance(), BOOKMAKER),
    BETSLIP_DELETING_PAGE(ShowBetslipDeletingPageCommand.getInstance(), BOOKMAKER),
    BETSLIP_DELETE(BetslipDeletingCommand.getInstance(), BOOKMAKER),
    BET_MANAGEMENT_PAGE(ShowBetManagementPageCommand.getInstance(), ADMINISTRATOR),
    BET_LIST_PAGE(ShowAllBetsViewingPageCommand.getInstance(), ADMINISTRATOR),
    SINGLE_BET_ADDING_PAGE(ShowSingleBetAddingPageCommand.getInstance(), USER),
    SINGLE_BET_ADD(SingleBetAddingCommand.getInstance(), USER),
    PARLAY_BET_ADDING_PAGE(ShowParlayBetAddingPageCommand.getInstance(), USER),
    PARLAY_BET_ADD(ParlayBetAddingCommand.getInstance(), USER),
    SYSTEM_BET_ADDING_PAGE(ShowSystemBetAddingPageCommand.getInstance(), USER),
    SYSTEM_BET_ADD(SystemBetAddingCommand.getInstance(), USER),
    BET_HISTORY_PAGE(ShowBetHistoryPageCommand.getInstance(), ADMINISTRATOR),
    COMMIT_COMPETITION_RESULTS(CompetitionResultsCommitCommand.getInstance(), ADMINISTRATOR),
    SELECT_LANGUAGE(LanguageSelectionCommand.getInstance()),
    SUCCESS(ShowSuccessPageCommand.getInstance()),
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