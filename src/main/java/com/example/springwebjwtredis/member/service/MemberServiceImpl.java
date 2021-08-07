package com.example.springwebjwtredis.member.service;

import com.example.springwebjwtredis.member.domain.Member;
import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.member.exception.MemberNotFoundException;
import com.example.springwebjwtredis.member.exception.PasswordNotEqualsException;
import com.example.springwebjwtredis.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDto addMember(MemberDto member) {

        Member savedMember = memberRepository.save(member.toEntity());

        return MemberDto.toDto(savedMember);
    }

    @Override
    public MemberDto updateMember(Long memberId, MemberDto member) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        findMember.updateInfo(member);
        return MemberDto.toDto(findMember);
    }

    @Override
    public MemberDto deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(member);
        return MemberDto.builder()
                .id(memberId)
                .build();
    }

    @Override
    public MemberDto findMember(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        // 패스워드 검증 후에 사용자 정보 조회
        if (!member.getPassword().equals(password)) {
            throw new PasswordNotEqualsException("사용자 패스워드 정보가 일치하지 않습니다.");
        }
        return MemberDto.toDto(member);
    }

}
