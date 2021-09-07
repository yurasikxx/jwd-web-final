package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

/**
 * Enum {@code BetslipType} is a betslip result. Implements {@code BaseEntity} interface.
 *
 * @see Betslip
 */
public enum BetslipType implements BaseEntity {

    HOME_TEAM_WIN(1L, "Home team win"),
    AWAY_TEAM_WIN(2L, "Away team win"),
    DRAW(3L, "Draw"),
    HOME_TEAM_WILL_NOT_LOSE(4L, "Home team won't lose"),
    AWAY_TEAM_WILL_NOT_LOSE(5L, "Away team won't lose"),
    NO_DRAW(6L, "No draw");

    private static final String UNKNOWN_BETSLIP_TYPE_ID_MSG = "Unknown betslip type ID: %s";
    private static final String UNKNOWN_BETSLIP_TYPE_NAME_MSG = "Unknown betslip type name: %s";

    private final Long id;
    private final String name;

    BetslipType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static BetslipType resolveBetslipTypeById(Long id) throws UnknownEnumAttributeException {
        for (BetslipType betslipType : values()) {
            if (betslipType.getId().equals(id)) {
                return betslipType;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BETSLIP_TYPE_ID_MSG, id));
    }

    public static BetslipType resolveBetslipTypeByName(String name) throws UnknownEnumAttributeException {
        for (BetslipType betslipType : values()) {
            if (betslipType.getName().equals(name)) {
                return betslipType;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BETSLIP_TYPE_NAME_MSG, name));
    }

}