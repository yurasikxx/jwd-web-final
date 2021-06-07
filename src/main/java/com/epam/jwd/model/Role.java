package com.epam.jwd.model;

public enum Role implements BaseEntity {

    ADMINISTRATOR(1L, "Administrator"),
    USER(2L, "User");

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

    @Override
    public String getName() {
        return name;
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