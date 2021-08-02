package com.example.springwebjwtredis.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

    private String email;

    private String password;

    private String name;

    @Builder
    public Member(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void udpateInfo(MemberDto member) {
        if (!member.getName().isEmpty()) {
            this.name = member.getName();
        }
        if (!member.getPassword().isEmpty()) {
            this.password = member.getPassword();
        }
    }
}
