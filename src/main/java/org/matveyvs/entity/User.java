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
//@Audited
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "User")
public class User implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer id;
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
