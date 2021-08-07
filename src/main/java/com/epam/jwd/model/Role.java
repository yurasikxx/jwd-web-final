package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

import java.util.Arrays;
import java.util.List;

/**
 * Enum {@code CompetitionResult} is a person role. Implements {@code BaseEntity} interface.
 *
 * @see Person
 */
public enum Role implements BaseEntity {

    ADMINISTRATOR(1L, "Administrator"),
    BOOKMAKER(2L, "Bookmaker"),
    USER(3L, "User"),
    UNAUTHORIZED(4L, "Unauthorized");

    private static final String UNKNOWN_ROLE_ID_MSG = "Unknown role ID: %s";
    private static final String UNKNOWN_ROLE_NAME_MSG = "Unknown role name: %s";
    private static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(Role.values());

    private final Long id;
    private final String name;

    /**
     * A {@code Role} constructor.
     *
     * @param id   a role ID.
     * @param name a role name.
     */
    Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @return a role name.
     */
    public String getName() {
        return name;
    }

    /**
     * Represents enum values as list elements.
     *
     * @return a role enum constants.
     */
    public static List<Role> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }

    /**
     * Looking for an enum constant by its ID.
     *
     * @param id a role ID.
     * @return an enum constant if it exists.
     * @throws UnknownEnumAttributeException if enum constant doesn't exist.
     */
    public static Role resolveRoleById(Long id) throws UnknownEnumAttributeException {
        for (Role role : values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_ROLE_ID_MSG, id));
    }

    /**
     * Looking for an enum constant by its name.
     *
     * @param name a role name.
     * @return an enum constant if it exists.
     * @throws UnknownEnumAttributeException if enum constant doesn't exist.
     */
    public static Role resolveRoleByName(String name) throws UnknownEnumAttributeException {
        for (Role role : values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_ROLE_NAME_MSG, name));
    }

}