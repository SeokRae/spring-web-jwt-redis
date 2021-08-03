package com.example.springwebjwtredis.dto;

import com.example.springwebjwtredis.domain.MemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RequestUpdateMember {
    private String email;
    private String name;
    private String password;

    public MemberDto toDto() {
        return MemberDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
