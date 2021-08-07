package com.example.springwebjwtredis.member.dto;

import com.example.springwebjwtredis.member.domain.Member;
import com.example.springwebjwtredis.member.domain.MemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestAddMember {
    private String email;
    private String name;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }

    public MemberDto toDto() {
        return MemberDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }

}
