package com.example.springwebjwtredis.repository;

import com.example.springwebjwtredis.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
