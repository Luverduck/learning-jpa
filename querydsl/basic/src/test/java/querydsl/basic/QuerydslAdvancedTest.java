package querydsl.basic;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.basic.entity.Member;
import querydsl.basic.entity.Team;

import java.util.List;

import static querydsl.basic.entity.QMember.member;

@SpringBootTest
@Transactional
public class QuerydslAdvancedTest {

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
        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
        // JPAQueryFactory 초기화
        queryFactory = new JPAQueryFactory(entityManager);
    }

    // 엔티티 반환
    @Test
    public void entityQuery() {
        // 조회 결과 반환
        List<Member> result = queryFactory
                                .select(member)
                                .from(member)
                                .fetch();
        // 조회 결과 확인
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    // 스칼라 프로젝션
    @Test
    public void scalarProjection() {
        // 조회 결과 반환
        List<String> result = queryFactory
                                .select(member.username)
                                .from(member)
                                .fetch();
        // 조회 결과 확인
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    // 튜플 프로젝션
    @Test
    public void tupleProjection() {
        // 조회 결과 반환
        List<Tuple> result = queryFactory
                                .select(member.username, member.age)
                                .from(member)
                                .fetch();
        // 조회 결과 확인
        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

}
