package com.example.springwebjwtredis.domain;

import com.example.springwebjwtredis.dto.ResponseMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = "password")
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String name;

    @Builder
    public MemberDto(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static ResponseMember toResponse(MemberDto memberDto) {
        return ResponseMember.builder()
                .id(memberDto.getId())
                .email(memberDto.getEmail())
                .name(memberDto.getName())
                .build();
    }

    public static MemberDto toDto(Member savedMember) {
        return MemberDto.builder()
                .id(savedMember.getId())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
