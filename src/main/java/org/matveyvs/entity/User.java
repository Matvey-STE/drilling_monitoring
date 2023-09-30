package org.matveyvs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer userId;
    @Column(name = "username")
    String userName;
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @Column(name = "creted_at")
    Timestamp createdAt;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
}
