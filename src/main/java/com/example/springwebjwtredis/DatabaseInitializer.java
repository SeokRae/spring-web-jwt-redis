package com.example.springwebjwtredis;

import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final MemberService memberService;

    public DatabaseInitializer(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        memberService.addMember(MemberDto.builder()
                        .email("seok@gmail.com")
                        .name("seok")
                        .password("1234")
                .build());
    }
}
