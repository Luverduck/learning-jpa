package querydsl.basic;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.basic.entity.Member;
import querydsl.basic.entity.QMember;
import querydsl.basic.entity.Team;

import java.util.List;

import static querydsl.basic.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    // EntityManager
    @Autowired
    EntityManager entityManager;

    // JPAQueryFactory
    JPAQueryFactory queryFactory;

    // 데이터베이스 초기화
    @BeforeEach
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
        Member memberNull = new Member(null, 100);
        Member member5 = new Member("member5", 100);
        Member member6 = new Member("member6", 100);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        entityManager.persist(memberNull);
        entityManager.persist(member5);
        entityManager.persist(member6);
        // JPAQueryFactory 초기화
        queryFactory = new JPAQueryFactory(entityManager);
    }

    // JPQL 방식
    @Test
    public void startJpql() {
        Member findMember = entityManager
                                .createQuery("select m from Member m where m.username = :username", Member.class)
                                .setParameter("username", "member1")
                                .getSingleResult();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // Querydsl 방식 (Q 타입 인스턴스 직접 생성)
    @Test
    public void startQuerydsl() {
        QMember m = new QMember("m");
        Member findMember = queryFactory
                                .selectFrom(m)
                                .from(m)
                                .where(m.username.eq("member1"))
                                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // Querydsl 방식 (Q 타입 정적 인스턴스)
    @Test
    public void startQuerydslWithQType() {
        QMember m = member;
        Member findMember = queryFactory
                                .selectFrom(m)
                                .from(m)
                                .where(m.username.eq("member1"))
                                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // 검색 조건 적용
    @Test
    public void search1() {
        Member findMember = queryFactory
                                .selectFrom(member)
                                .from(member)
                                .where(member.username.eq("member1"))
                                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // 검색 조건 적용 (복합 조건 검색)
    @Test
    public void search2() {
        Member findMember = queryFactory
                                .selectFrom(member)
                                .from(member)
                                .where(
                                    member.username.eq("member1"),
                                    member.age.eq(10)
                                )
                                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // 조회 결과 반환
    @Test
    public void resultFetch() {
        // 조회 결과를 List<T>로 반환 (결과가 없으면 비어 있는 List<T> 반환)
        List<Member> fetch = queryFactory
                                .selectFrom(member)
                                .fetch();
        // 단건 조회 결과 반환 (결과가 없으면 null, 둘 이상이면 NonUniqueResultException 발생)
        Member fetchOne = queryFactory
                                .selectFrom(member)
                                .fetchOne();
        // 조회 결과 중 첫 번째 한 건 반환 (결과가 없으면 null)
        Member fetchFirst = queryFactory
                                .selectFrom(member)
                                .fetchFirst();
    }

    // 조회 결과 및 조회 결과 수 반환
    @Test
    public void resultFetchAndCount() {
        // 조회 결과 반환
        List<Member> fetchResult = queryFactory
                                    .select(member)
                                    .from(member)
                                    .fetch();
        // 조회 결과 수 반환
        Long fetchResultCount = queryFactory
                                    .select(member.count())
                                    .from(member)
                                    .fetchOne();
    }

    /**
     * 정렬
     * 1. 회원 나이 기준 내림차순 (desc)
     * 2. 회원 이름 기준 올림차순 (asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력 (nullsLast)
     */
    @Test
    public void sort() {
        List<Member> result = queryFactory
                                .selectFrom(member)
                                .where(member.age.eq(100))
                                .orderBy(
                                    member.age.desc(),
                                    member.username.asc().nullsLast()
                                )
                                .fetch();
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        Assertions.assertThat(member5.getUsername()).isEqualTo("member5");
        Assertions.assertThat(member6.getUsername()).isEqualTo("member6");
        Assertions.assertThat(memberNull.getUsername()).isNull();
    }

}