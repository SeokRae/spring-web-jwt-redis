package com.example.springwebjwtredis.service;

import com.example.springwebjwtredis.aop.exception.MemberNotFoundException;
import com.example.springwebjwtredis.aop.exception.PasswordNotEqualsException;
import com.example.springwebjwtredis.domain.Member;
import com.example.springwebjwtredis.domain.MemberDto;
import com.example.springwebjwtredis.repository.MemberRepository;
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
        findMember.udpateInfo(member);
        return MemberDto.toDto(findMember);
    }

    /**
     * TODO 세션으로 비밀번호 가져오든, 인가 처리를 통해 하던지 비번 비교해서 사용자 맞으면 삭제
     *
     * @param memberId
     * @return
     */
    @Override
    public MemberDto deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        // 삭제 전 사용자 검증
        memberRepository.delete(member);
        return MemberDto.builder().id(memberId).build();
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
