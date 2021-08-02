package com.example.springwebjwtredis.service;

import com.example.springwebjwtredis.domain.MemberDto;

public interface MemberService {
    MemberDto addMember(MemberDto member);

    MemberDto updateMember(Long memberId, MemberDto member);

    MemberDto deleteMember(Long memberId);
}
