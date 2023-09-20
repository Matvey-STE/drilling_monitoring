package org.matveyvs.dto;

import java.sql.Timestamp;

public record CreateUserDto(String username, String email, String password, String role,
                            Timestamp createdAt, String firstName, String lastName) {
}
