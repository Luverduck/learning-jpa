package springdatajpa.basic.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springdatajpa.basic.dto.MemberDto;
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

    @GetMapping("/members1")
    public Page<Member> findMemberList(@PageableDefault(page = 1, size = 5) Pageable pageable) {
        Page<Member> page =  memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members2")
    public Page<MemberDto> findMemberDtoList(Pageable pageable) {
        Page<Member> page =  memberRepository.findAll(pageable);
        Page<MemberDto> result = page.map(member -> new MemberDto(member));
        return result;
    }

    // MemberController 빈 초기화 후 실행
    // @PostConstruct
    public void init() {
        for (int i = 0; i < 100; ++i) {
            Member member = new Member("user" + i);
            memberRepository.save(member);
        }
    }

}