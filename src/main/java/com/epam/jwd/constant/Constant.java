package com.epam.jwd.constant;

public class Constant {

    public static final String MAIN_JSP_PATH = "/jsp/main.jsp";
    public static final String REGISTER_JSP_PATH = "/jsp/registration.jsp";
    public static final String LOGIN_JSP_PATH = "/jsp/login.jsp";
    public static final String ADDING_JSP_PATH = "/jsp/adding.jsp";
    public static final String VIEWING_JSP_PATH = "/jsp/viewing.jsp";
    public static final String CHANGING_JSP_PATH = "/jsp/changing.jsp";
    public static final String DELETING_JSP_PATH = "/jsp/deleting.jsp";
    public static final String INDEX_JSP_PATH = "/index.jsp";

    public static final String PERSON_ATTRIBUTE_NAME = "person";
    public static final String SELECT_PERSON_ATTRIBUTE_NAME = "selectPerson";
    public static final String COMPETITION_ATTRIBUTE_NAME = "competition";
    public static final String SELECT_TEAM_ATTRIBUTE_NAME = "selectTeam";
    public static final String SELECT_COMPETITION_ATTRIBUTE_NAME = "selectCompetition";
    public static final String BETSLIP_ATTRIBUTE_NAME = "betslip";
    public static final String SELECT_BET_TYPE_ATTRIBUTE_NAME = "selectBetType";
    public static final String SELECT_BETSLIP_ATTRIBUTE_NAME = "selectBetslip";
    public static final String BET_ATTRIBUTE_NAME = "bet";
    public static final String BET_HISTORY_ATTRIBUTE_NAME = "betHistory";
    public static final String ERROR_ATTRIBUTE_NAME = "error";
    public static final String LANGUAGE_ATTRIBUTE_NAME = "language";
    public static final String PERSON_BALANCE_SESSION_ATTRIBUTE_NAME = "personBalance";
    public static final String PERSON_NAME_SESSION_ATTRIBUTE_NAME = "personName";
    public static final String PERSON_ROLE_SESSION_ATTRIBUTE_NAME = "personRole";

    public static final String ID_PARAMETER_NAME = "id";
    public static final String LOGIN_PARAMETER_NAME = "login";
    public static final String PASSWORD_PARAMETER_NAME = "password";
    public static final String AWAY_TEAM_PARAMETER_NAME = "awayTeamId";
    public static final String HOME_TEAM_PARAMETER_NAME = "homeTeamId";
    public static final String COMPETITION_PARAMETER_NAME = "competitionId";
    public static final String BET_TYPE_PARAMETER_NAME = "betTypeId";
    public static final String COEFFICIENT_PARAMETER_NAME = "coefficient";
    public static final String BETSLIP_PARAMETER_NAME = "betslipId";
    public static final String BET_TOTAL_PARAMETER_NAME = "betTotal";
    public static final String LOCALE_PARAMETER_NAME = "locale";

    public static final String ERROR_MESSAGE_KEY = "error.msg";
    public static final String TRY_AGAIN_MESSAGE_KEY = "try.again";
    public static final String FIELDS_FILLED_MESSAGE_KEY = "fields.filled";
    public static final String TEAM_DIFFERENCE_MESSAGE_KEY = "competition.team.difference";
    public static final String TEAM_SPORT_DIFFERENCE_MESSAGE_KEY = "competition.team.sport.difference";
    public static final String ID_EMPTY_MESSAGE_KEY = "id.empty";
    public static final String NUMBERS_POSITIVE_MESSAGE_KEY = "numbers.positive";
    public static final String INCORRECT_DATA_MESSAGE_KEY = "data.incorrect";
    public static final String BETSLIP_ALREADY_EXISTS_MESSAGE_KEY = "betslip.already.exists";
    public static final String PERSON_HAS_BET_MESSAGE_KEY = "person.has.bet";
    public static final String EMPTY_CREDENTIALS_MESSAGE_KEY = "credentials.empty";
    public static final String EMPTY_USER_MESSAGE_KEY = "person.empty";

    public static final String FAILED_TO_SAVE_ENTITY_MSG = "Failed to save entity";
    public static final String FAILED_TO_FIND_PREPARED_ENTITIES_MSG = "Failed to find prepared entities";
    public static final String FAILED_TO_UPDATE_ENTITY_MSG = "Failed to update entity";
    public static final String FAILED_TO_DELETE_ENTITY_MSG = "Failed to delete entity";
    public static final String ENTITY_WAS_SAVED_MSG = "Entity was saved";
    public static final String ENTITY_WAS_NOT_SAVED_MSG = "Entity wasn't saved";
    public static final String ENTITY_WAS_UPDATED_MSG = "Entity was updated";
    public static final String ENTITY_WAS_NOT_UPDATED_MSG = "Entity wasn't updated";
    public static final String ENTITY_WAS_DELETED_MSG = "Entity was deleted";
    public static final String ENTITY_WAS_NOT_DELETED_MSG = "Entity wasn't deleted";
    public static final String PREPARED_ENTITIES_WERE_FOUND = "Prepared entities were found";
    public static final String PREPARED_ENTITIES_WERE_NOT_FOUND_MSG = "Prepared entities weren't found";
    public static final String ENTITIES_WERE_FOUND_MSG = "Entities were found";
    public static final String ENTITIES_WERE_NOT_FOUND_MSG = "Entities weren't found";

    public static final int EMPTY_LIST_SIZE_VALUE = 0;
    public static final int INITIAL_ID_VALUE = 1;
    public static final int INDEX_ROLLBACK_VALUE = INITIAL_ID_VALUE;
    public static final int INITIAL_INDEX_VALUE = INITIAL_ID_VALUE;
    public static final int MIN_INDEX_VALUE = EMPTY_LIST_SIZE_VALUE;
    public static final int INITIAL_BALANCE_VALUE = 1000;
    public static final Long MIN_LONG_ID_VALUE = 1L;

    private Constant() {
    }

}