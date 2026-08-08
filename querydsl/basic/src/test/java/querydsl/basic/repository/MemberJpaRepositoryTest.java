package querydsl.basic.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import querydsl.basic.dto.MemberDto;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.dto.MemberTeamDto;
import querydsl.basic.entity.Member;
import querydsl.basic.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    // 순수 JPA 기본 테스트
    @Test
    public void basicJpaTest() {
        // 엔티티 저장
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);
        // 엔티티 단일 조회
        Member result1 = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(result1).isEqualTo(member);
        // 엔티티 전체 조회
        List<Member> result2 = memberJpaRepository.findAll();
        Assertions.assertThat(result2).containsExactly(member);
        // 엔티티 조건 조회
        List<Member> result3 = memberJpaRepository.findByUsername("member1");
        Assertions.assertThat(result3).containsExactly(member);
    }

    // QueryDSL 기본 테스트
    @Test
    public void basicQuerydslTest() {
        // 엔티티 저장
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);
        // 엔티티 단일 조회
        Member result1 = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(result1).isEqualTo(member);
        // 엔티티 전체 조회
        List<Member> result2 = memberJpaRepository.findAllQuerydsl();
        Assertions.assertThat(result2).containsExactly(member);
        // 엔티티 조건 조회
        List<Member> result3 = memberJpaRepository.findByUsernameQuerydsl("member1");
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

    // 조회 최적화 - Projections
    @Test
    public void searchDtoByProjectionsTest() {
        // 데이터 초기화
        init();
        // DTO 프로젝션
        List<MemberDto> result = memberJpaRepository.searchDtoByProjections();
        // 조회 결과 확인
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // 조회 최적화 - @QueryProjection
    @Test
    public void searchDtoByQueryProjection() {
        // 데이터 초기화
        init();
        // DTO 프로젝션
        List<MemberDto> result = memberJpaRepository.searchDtoByQueryProjection();
        // 조회 결과 확인
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // 동적 쿼리 - BooleanBuilder 방식
    @Test
    public void searchByBooleanBuilderTest() {
        // 데이터 초기화
        init();
        // 조건 생성
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeMin(35);
        condition.setAgeMax(40);
        condition.setTeamName("teamB");
        // 동적 쿼리
        List<MemberTeamDto> result = memberJpaRepository.searchByBooleanBuilder(condition);
        // 검증
        Assertions.assertThat(result).extracting("username").containsExactly("member4");
    }

    // 동적 쿼리 - where() 다중 파라미터 방식
    @Test
    public void searchByWhereMultiParameterTest() {
        // 데이터 초기화
        init();
        // 조건 생성
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeMin(35);
        condition.setAgeMax(40);
        condition.setTeamName("teamB");
        // 동적 쿼리
        List<MemberTeamDto> result = memberJpaRepository.searchByBooleanBuilder(condition);
        // 검증
        Assertions.assertThat(result).extracting("username").containsExactly("member4");
    }

}