package org.matveyvs.dto.repo;

import org.matveyvs.entity.Role;

import java.sql.Timestamp;

public record UserReadDto(Integer id, String userName, String email, String password,
                          Role role, Timestamp createdAt, String firstName, String lastName) {
}
