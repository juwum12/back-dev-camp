package com.sparta.backdevcamp.auth.entity;

import com.sparta.backdevcamp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String username;
    @Column
    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.USER;
    @OneToMany(mappedBy = "user")
    private List<AccessLog> accessLog;

    public User() {}

    public User(String email, String password, String username, UserRole role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
