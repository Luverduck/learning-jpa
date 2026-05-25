package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    // 회원 가입
    @Test
    public void join() {
        // 회원 생성
        Member member = new Member();
        member.setName("Kim");
        // 회원 가입
        Long savedId = memberService.join(member);
        // 가입한 회원과 DB에서 조회한 회원의 동일 여부 검증
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    // 회원 가입 (회원 이름 중복 예외 발생)
    @Test
    public void joinDuplicateName() {
        // 회원 생성
        Member member1 = new Member();
        member1.setName("Kim");
        Member member2 = new Member();
        member2.setName("Kim");
        // member1 회원 가입
        memberService.join(member1);
        // member2 회원 가입 > 회원 이름 중복으로 IllegalStateException 발생
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }

}