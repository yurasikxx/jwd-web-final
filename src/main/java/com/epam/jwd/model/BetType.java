package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

public enum BetType implements BaseEntity {

    HOME_TEAM_WIN(1L, "Home team win"),
    AWAY_TEAM_WIN(2L, "Away team win"),
    DRAW(3L, "Draw"),
    HOME_TEAM_WILL_NOT_LOSE(4L, "Home team won't lose"),
    AWAY_TEAM_WILL_NOT_LOSE(5L, "Away team won't lose"),
    NO_DRAW(6L, "No draw");

    private static final String UNKNOWN_BET_TYPE_ID_MSG = "Unknown bet type ID: %s";
    private static final String UNKNOWN_BET_TYPE_NAME_MSG = "Unknown bet type name: %s";

    private final Long id;
    private final String name;

    BetType(Long id, String name) {
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

    public static BetType resolveBetTypeById(Long id) throws UnknownEnumAttributeException {
        for (BetType betType : values()) {
            if (betType.getId().equals(id)) {
                return betType;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BET_TYPE_ID_MSG, id));
    }

    public static BetType resolveBetTypeByName(String name) throws UnknownEnumAttributeException {
        for (BetType betType : values()) {
            if (betType.getName().equals(name)) {
                return betType;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BET_TYPE_NAME_MSG, name));
    }

}