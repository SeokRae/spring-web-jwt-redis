package com.example.springwebjwtredis.member.service;

import com.example.springwebjwtredis.member.domain.MemberDto;

public interface MemberService {
    MemberDto addMember(MemberDto member);

    MemberDto updateMember(Long memberId, MemberDto member);

    MemberDto deleteMember(Long memberId);

    MemberDto findMember(String email, String password);

}
