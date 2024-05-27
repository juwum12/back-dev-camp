package com.sparta.backdevcamp.auth.entity;

import com.sparta.backdevcamp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class TokenBlackList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String token;
    @Column
    private String jti;
    @Column
    @Enumerated(value = EnumType.STRING)
    private TokenType tokenType;
    @Column
    private Date expiresAt;

    public TokenBlackList(){
    }

    public TokenBlackList(String token, String jti, TokenType tokenType, Date expiresAt) {
        this.token = token;
        this.jti = jti;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
    }
}
