package org.matveyvs.dto;

import org.matveyvs.entity.Role;

import java.sql.Timestamp;

public record UserDto(Integer id, String username, String email, String password,
                      Role role, Timestamp createdAt, String firstName, String lastName) {
}
