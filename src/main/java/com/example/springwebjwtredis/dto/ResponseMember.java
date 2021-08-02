package com.example.springwebjwtredis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseMember {
    private Long id;
    private String email;
    private String name;

    @Builder
    public ResponseMember(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
