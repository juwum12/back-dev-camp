package com.sparta.backdevcamp.auth.entity;

import com.sparta.backdevcamp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class AccessLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String ua;

    private String ip;

    private String endpoint;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AccessLog(){
    }

    public AccessLog(String ua, String ip, String endpoint, User user) {
        this.ua = ua;
        this.ip = ip;
        this.endpoint = endpoint;
        this.user = user;
    }
}
