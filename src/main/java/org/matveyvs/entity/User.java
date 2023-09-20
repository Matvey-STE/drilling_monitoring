package org.matveyvs.entity;

import java.sql.Timestamp;

public record User(Long id, String userName, String email, String password, Role role,
                   Timestamp createdAt, String firstName,
                   String lastName) {
    public User(String userName, String email, String password, Role role,
                Timestamp createdAt, String firstName,
                String lastName) {
        this(null, userName, email, password, role, createdAt, firstName,
                lastName);
    }
}
