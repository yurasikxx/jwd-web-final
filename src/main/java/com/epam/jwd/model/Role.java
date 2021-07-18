package com.epam.jwd.model;

import java.util.Arrays;
import java.util.List;

public enum Role implements BaseEntity {

    ADMINISTRATOR(1L, "Administrator"),
    USER(2L, "User"),
    UNAUTHORIZED(3L, "Unauthorized");

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

    public static Role resolveRoleById(Long id) {
        for (Role role : values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Unknown role ID");
    }

    public static Role resolveRoleByName(String name) {
        for (Role role : values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Unknown role name");
    }

}