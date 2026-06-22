package springdatajpa.basic.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springdatajpa.basic.entity.Member;
import springdatajpa.basic.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/member1/{id}")
    public String findMember1(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/member2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // MemberController 빈 초기화 후 실행
    @PostConstruct
    public void init() {
        Member member = new Member("userA");
        memberRepository.save(member);
        System.out.println("member.getId() = " + member.getId());
    }

}