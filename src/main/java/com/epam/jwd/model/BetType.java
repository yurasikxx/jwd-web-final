package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

/**
 * Enum {@code BetType} is a bet result. Implements {@code BaseEntity} interface
 *
 * @see Bet
 */
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

    /**
     * A {@code BetType} constructor.
     *
     * @param id   a bet type ID.
     * @param name a bet type name.
     */
    BetType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return a bet type name.
     */
    public String getName() {
        return name;
    }

    /**
     * Looking for an enum constant by its ID.
     *
     * @param id a bet type ID.
     * @return an enum constant if it exists.
     * @throws UnknownEnumAttributeException if enum constant doesn't exist.
     */
    public static BetType resolveBetTypeById(Long id) throws UnknownEnumAttributeException {
        for (BetType betType : values()) {
            if (betType.getId().equals(id)) {
                return betType;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BET_TYPE_ID_MSG, id));
    }

    /**
     * Looking for an enum constant by its name.
     *
     * @param name a bet type name.
     * @return an enum constant if it exists.
     * @throws UnknownEnumAttributeException if enum constant doesn't exist.
     */
    public static BetType resolveBetTypeByName(String name) throws UnknownEnumAttributeException {
        for (BetType betType : values()) {
            if (betType.getName().equals(name)) {
                return betType;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_BET_TYPE_NAME_MSG, name));
    }

}