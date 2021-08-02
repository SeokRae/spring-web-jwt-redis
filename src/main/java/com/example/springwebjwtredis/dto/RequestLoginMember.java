package com.example.springwebjwtredis.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestLoginMember {
    private String email;
    private String password;
}
