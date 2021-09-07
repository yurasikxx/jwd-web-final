package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

/**
 * Enum {@code BetType} is a bet result. Implements {@code BaseEntity} interface.
 *
 * @see Bet
 */
public enum BetType implements BaseEntity {

    SINGLE(1L, "Single"),
    PARLAY(2L, "Parlay"),
    SYSTEM(3L, "System");

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