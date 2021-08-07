package com.example.springwebjwtredis.refresh.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String email;
    private String username;

    @Column(columnDefinition = "varchar(500)")
    private String refreshToken;

    @Builder
    public RefreshTokenEntity(Long id, String email, String username, String refreshToken) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
