package com.epam.jwd.model;

import com.epam.jwd.exception.UnknownEnumAttributeException;

import java.util.Arrays;
import java.util.List;

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

    Role(Long id, String name) {
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

    public static List<Role> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }

    public static Role resolveRoleById(Long id) throws UnknownEnumAttributeException {
        for (Role role : values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }

        throw new UnknownEnumAttributeException(String.format(UNKNOWN_ROLE_ID_MSG, id));
    }

    public static Role resolveRoleByName(String name) {
        for (Role role : values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }

        throw new IllegalArgumentException(String.format(UNKNOWN_ROLE_NAME_MSG, name));
    }

}