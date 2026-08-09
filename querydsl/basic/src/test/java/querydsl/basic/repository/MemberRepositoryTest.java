package querydsl.basic.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.dto.MemberTeamDto;
import querydsl.basic.entity.Member;
import querydsl.basic.entity.QMember;
import querydsl.basic.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    // 스프링 데이터 JPA 리포지토리 - 스프링 데이터 JPA 기본 테스트
    @Test
    public void basicSpringDataJpaTest() {
        // 엔티티 저장
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        // 엔티티 단일 조회
        Member result1 = memberRepository.findById(member.getId()).get();
        Assertions.assertThat(result1).isEqualTo(member);
        // 엔티티 전체 조회
        List<Member> result2 = memberRepository.findAll();
        Assertions.assertThat(result2).containsExactly(member);
        // 엔티티 조건 조회
        List<Member> result3 = memberRepository.findByUsername("member1");
        Assertions.assertThat(result3).containsExactly(member);
    }

    // 데이터 초기화
    public void init() {
        // Team 생성
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);
        // Member 생성
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
    }

    // 스프링 데이터 JPA 리포지토리 - QueryDSL 기본 테스트
    @Test
    public void basicQuerydslTest() {
        // 데이터 초기화
        init();
        // 조건 생성
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeMin(35);
        condition.setAgeMax(40);
        condition.setTeamName("teamB");
        // 동적 쿼리
        List<MemberTeamDto> result = memberRepository.search(condition);
        // 검증
        Assertions.assertThat(result).extracting("username").containsExactly("member4");
    }

    // 페이징
    @Test
    public void pagingTest() {
        // 데이터 초기화
        init();
        // 조건 생성
        MemberSearchCondition condition = new MemberSearchCondition();
        // 페이징 조건 생성
        PageRequest pageRequest = PageRequest.of(0, 3);
        // 조회 결과 반환
        Page<MemberTeamDto> result = memberRepository.searchPage(condition, pageRequest);
        // 검증
        Assertions.assertThat(result.getSize()).isEqualTo(3);
        Assertions.assertThat(result.getContent()).extracting("username").containsExactly("member1", "member2", "member3");
    }

    // QuerydslPredicateExecutor
    @Test
    public void querydslPredicateExecutorTest() {
        // 데이터 초기화
        init();
        // 조회 결과 반환
        QMember member = QMember.member;
        Iterable<Member> result = memberRepository.findAll(member.age.between(10, 40).and(member.username.eq("member1")));
        // 조회 결과 확인
        for (Member findMember : result) {
            System.out.println("findMember = " + findMember);
        }
    }

}
