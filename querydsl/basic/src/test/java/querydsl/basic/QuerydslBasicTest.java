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
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
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

}