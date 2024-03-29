package org.matveyvs.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN, TECH_SUPPORT, USER;

    public static Optional<Role> find(String role) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(role))
                .findFirst();
    }
}
