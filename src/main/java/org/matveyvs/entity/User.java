package org.matveyvs.entity;

import java.sql.Timestamp;

public record User(Long id, String userName, String email, String password, Status status,
                   Timestamp createdAt, Timestamp lastLoginAt, String firstName,
                   String lastName) {
    public User(String userName, String email, String password, Status status,
                Timestamp createdAt, Timestamp lastLoginAt, String firstName,
                String lastName) {
        this(null, userName, email, password, status, createdAt, lastLoginAt, firstName,
                lastName);
    }
}
