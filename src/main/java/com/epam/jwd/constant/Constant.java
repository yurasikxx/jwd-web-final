package com.epam.jwd.constant;

public class Constant {

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
    public static final String PERSON_PARAMETER_NAME = "personId";

    public static final String SOMETHING_WENT_WRONG_MSG = "Something went wrong...";
    public static final String ALL_FIELDS_MUST_BE_FILLED_MSG = "All fields must be filled";
    public static final String TEAM_DOES_NOT_EXIST_MSG = "Team with such ID doesn't exist";
    public static final String TEAMS_MUST_BE_DIFFERENT_MSG = "Teams must be different";
    public static final String TEAMS_MUST_BE_FROM_THE_SAME_SPORT_MSG = "Teams must be from the same sport";
    public static final String TRY_AGAIN_MSG = "Try again";
    public static final String EMPTY_ID_SENT_MSG = "ID was not entered";
    public static final String NUMBERS_MUST_BE_POSITIVE_MSG = "Numbers must be positive";
    public static final String INCORRECT_ENTERED_DATA = ALL_FIELDS_MUST_BE_FILLED_MSG + " and must be numbers";
    public static final String UNKNOWN_BET_TYPE_ID_MSG = "Unknown bet type ID";
    public static final String WRONG_COMPETITION_ID_MSG = "Competition with such ID doesn't exist";
    public static final String BETSLIP_ALREADY_EXISTS_MSG = "Betslip of given competition with such bet type already exists";
    public static final String BET_SUCCESSFULLY_ADDED_MSG = "Bet successfully added";
    public static final String BETSLIP_DOES_NOT_EXIST_MSG = "Betslip with such ID doesn't exist";
    public static final String PERSON_DOES_NOT_EXIST_MSG = "Person with such ID doesn't exist";
    public static final String PERSON_ALREADY_HAS_BET_WITH_SUCH_BETSLIP_MSG = "Person already has bet with such betslip";
    public static final String EMPTY_CREDENTIALS_MSG = "Credentials must not be empty";

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