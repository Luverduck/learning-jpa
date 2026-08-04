package querydsl.basic;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.basic.dto.MemberDto;
import querydsl.basic.dto.UserDto;
import querydsl.basic.entity.Member;
import querydsl.basic.entity.QMember;
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

    // 튜플 프로젝션
    @Test
    public void findTupleByProjectionTuple() {
        // 조회 결과 반환
        List<Tuple> result = queryFactory
                                    .select(
                                        Projections.tuple(member.username, member.age)
                                    )
                                    .from(member)
                                    .fetch();
        // 조회 결과 확인
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    // DTO 프로젝션 - 순수 JPA (JPQL)
    @Test
    public void findDtoByJPQL() {
        // 조회 결과 반환
        List<MemberDto> result = entityManager
                                    .createQuery(
                                        "select new querydsl.basic.dto.MemberDto(m.username, m.age) from Member m",
                                        MemberDto.class
                                    )
                                    .getResultList();
        // 조회 결과 확인
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // DTO 프로젝션 - Bean 프로젝션 ( Projections.bean() )
    @Test
    public void findDtoByProjectionBean() {
        // 조회 결과 반환
        List<MemberDto> result = queryFactory
                                    .select(
                                        Projections.bean(MemberDto.class, member.username, member.age)
                                    )
                                    .from(member)
                                    .fetch();
        // 조회 결과 확인
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // DTO 프로젝션 - Bean 프로젝션 ( Projections.fields() )
    @Test
    public void findDtoByProjectionField() {
        // 조회 결과 반환
        List<MemberDto> result = queryFactory
                                    .select(
                                        Projections.fields(MemberDto.class, member.username, member.age)
                                    )
                                    .from(member)
                                    .fetch();
        // 조회 결과 확인
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    // DTO 프로젝션 - Bean 프로젝션 ( 프로젝션 대상과 DTO 프로퍼티의 이름이 다를 경우 )
    @Test
    public void findDtoByProjection() {
        QMember memberSub = new QMember("memberSub");
        // 조회 결과 반환
        List<UserDto> result = queryFactory
                                    .select(
                                        Projections.fields(
                                            UserDto.class,
                                            member.username.as("name"),
                                            ExpressionUtils.as(
                                                JPAExpressions.select(memberSub.age.max()).from(memberSub),
                                                "age"
                                            )
                                        )
                                    )
                                    .from(member)
                                    .fetch();
        // 조회 결과 확인
        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    // DTO 프로젝션 - 생성자 프로젝션 ( Projections.constructor() )
    @Test
    public void findDtoByProjectionConstructor() {
        // 조회 결과 반환
        List<MemberDto> result = queryFactory
                                    .select(
                                        Projections.constructor(MemberDto.class, member.username, member.age)
                                    )
                                    .from(member)
                                    .fetch();
        // 조회 결과 확인
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

}
