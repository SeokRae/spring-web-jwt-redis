package com.example.springwebjwtredis.refresh.service;

import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.refresh.domain.RefreshTokenEntity;

public interface RefreshTokenService {
    void save(MemberDto memberDto, String refreshToken);

    RefreshTokenEntity findByEmail(String email);
}
