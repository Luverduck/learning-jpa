package querydsl.basic.controller;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import querydsl.basic.entity.Member;
import querydsl.basic.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class MemberQuerydslController {

    private final MemberRepository memberRepository;

    @GetMapping("/querydsl/members")
    public Iterable<Member> searchMemberV3(@QuerydslPredicate(root = Member.class) Predicate predicate, Pageable pageable) {
        return memberRepository.findAll(predicate, pageable);
    }

}
