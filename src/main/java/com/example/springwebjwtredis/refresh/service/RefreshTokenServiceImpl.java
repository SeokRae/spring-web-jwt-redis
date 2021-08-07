package com.example.springwebjwtredis.refresh.service;

import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.refresh.domain.RefreshTokenEntity;
import com.example.springwebjwtredis.refresh.exception.NotFoundRefreshTokenException;
import com.example.springwebjwtredis.refresh.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void save(MemberDto memberDto, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .email(memberDto.getEmail())
                .username(memberDto.getName())
                .refreshToken(refreshToken)
                .build();
        // refresh token 저장
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public RefreshTokenEntity findByEmail(String email) {
        return refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundRefreshTokenException("RefreshToken 값이 존재하지 않습니다."));
    }
}
