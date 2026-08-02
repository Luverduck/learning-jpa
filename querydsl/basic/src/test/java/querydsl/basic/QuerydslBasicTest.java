package querydsl.basic;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
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
import static querydsl.basic.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    // EntityManager
    @Autowired
    EntityManager entityManager;

    // EntityManagerFactory
    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

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

    // 데이터 저장 (JPAInsertClause)
    @Test
    public void insertClause() {
        // JPAInsertClause
        long result = queryFactory
                                .insert(member)
                                .set(member.username, "member8")
                                .set(member.age, 50)
                                .execute();
        // 검증
        Member findMember = queryFactory
                                .selectFrom(member)
                                .where(member.username.eq("member8"))
                                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member8");
    }

    // 데이터 수정 (JPAUpdateClause)
    @Test
    public void updateClause() {
        // 준비
        long result = queryFactory
                                .insert(member)
                                .set(member.username, "member8")
                                .set(member.age, 50)
                                .execute();
        // JPAUpdateClause
        long updateResult = queryFactory
                                .update(member)
                                .set(member.username, "member9")
                                .set(member.age, 60)
                                .where(member.username.eq("member8"))
                                .execute();
        entityManager.flush();
        entityManager.clear();
        // 검증
        Member findMember = queryFactory
                                .selectFrom(member)
                                .where(member.username.eq("member9"))
                                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member9");
        Assertions.assertThat(findMember.getAge()).isEqualTo(60);
    }

    // 데이터 삭제 (JPADeleteClause)
    @Test
    public void deleteClause() {
        // 준비
        Member target = new Member("updateTarget", 50);
        entityManager.persist(target);
        Long targetId = target.getId();
        entityManager.flush();
        entityManager.clear();
        // JPADeleteClause
        long deleteResult = queryFactory
                                .delete(member)
                                .where(member.id.eq(targetId))
                                .execute();
        entityManager.flush();
        entityManager.clear();
        // 검증
        Member findMember = queryFactory
                                .selectFrom(member)
                                .where(member.id.eq(targetId))
                                .fetchOne();
        Assertions.assertThat(findMember).isNull();
    }

    // 데이터 조회 (JPAQuery<T>)
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

    // 정렬 테스트를 위한 데이터 초기화
    public void initForSort() {
        Member memberNull = new Member(null, 100);
        Member member5 = new Member("member5", 100);
        Member member6 = new Member("member6", 100);
        entityManager.persist(memberNull);
        entityManager.persist(member5);
        entityManager.persist(member6);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 정렬
     * 1. 회원 나이 기준 내림차순 (desc)
     * 2. 회원 이름 기준 올림차순 (asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력 (nullsLast)
     */
    @Test
    public void sort() {
        // 정렬 테스트를 위한 초기화 데이터 추가
        initForSort();
        // 조회 결과 반환
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
        // 검증
        Assertions.assertThat(member5.getUsername()).isEqualTo("member5");
        Assertions.assertThat(member6.getUsername()).isEqualTo("member6");
        Assertions.assertThat(memberNull.getUsername()).isNull();
    }

    // 페이징
    @Test
    public void paging() {
        // 조회 결과 반환
        List<Member> result = queryFactory
                                .selectFrom(member)
                                .orderBy(member.username.desc())
                                .offset(1)
                                .limit(2)
                                .fetch();
        // 검증
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    // 집계
    @Test
    public void aggregation() {
        // 조회 결과 반환
        List<Tuple> result = queryFactory
                                .select(
                                    member.count(),
                                    member.age.sumLong(),
                                    member.age.avg(),
                                    member.age.min(),
                                    member.age.max()
                                )
                                .from(member)
                                .fetch();
        // 검증
        Tuple tuple = result.get(0);
        Assertions.assertThat(tuple.get(member.count())).isEqualTo(4);
        Assertions.assertThat(tuple.get(member.age.sumLong())).isEqualTo(100);
        Assertions.assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        Assertions.assertThat(tuple.get(member.age.max())).isEqualTo(40);
        Assertions.assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    // 그룹화
    @Test
    public void grouping() {
        // 조회 결과 반환
        // TODO: 팀별로 팀명과 평균 연령 조회 >> { 팀명, 평균 연령 }
        List<Tuple> result = queryFactory
                                .select(
                                    team.name,
                                    member.age.avg()
                                )
                                .from(member)
                                .join(member.team, team)
                                .groupBy(team.name)
                                .fetch();
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);
        // 검증
        Assertions.assertThat(teamA.get(team.name)).isEqualTo("teamA");
        Assertions.assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        Assertions.assertThat(teamB.get(team.name)).isEqualTo("teamB");
        Assertions.assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    // 조인 테스트를 위한 데이터 초기화
    public void initForJoin() {
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));
        entityManager.flush();
        entityManager.clear();
    }

    // 기본 조인 (내부 조인)
    @Test
    public void innerJoin() {
        // 조인 테스트를 위한 데이터 초기화
        initForJoin();
        // 조회 결과 반환
        // TODO: 팀 A에 소속된 모든 회원 조회
        List<Member> result = queryFactory
                                .selectFrom(member)
                                .join(member.team, team)
                                .fetch();
        // 검증
        Assertions.assertThat(result).extracting("username").containsExactly("member1", "member2", "member3", "member4");
    }

    // 기본 조인 (외부 조인)
    @Test
    public void outerJoin() {
        // 조인 테스트를 위한 데이터 초기화
        initForJoin();
        // 조회 결과 반환
        List<Member> result = queryFactory
                                .selectFrom(member)
                                .leftJoin(member.team, team)
                                .fetch();
        // 검증
        Assertions.assertThat(result).extracting("username").containsExactly("member1", "member2", "member3", "member4", "teamA", "teamB");
    }

    // 기본 조인 (세타 조인)
    @Test
    public void thetaJoin() {
        // 조인 테스트를 위한 데이터 초기화
        initForJoin();
        // 조회 결과 반환
        List<Member> result = queryFactory
                                .select(member)
                                .from(member, team)
                                .where(member.username.eq(team.name))
                                .orderBy(member.username.asc())
                                .fetch();
        // 검증
        Assertions.assertThat(result).extracting("username").containsExactly("teamA", "teamB");
    }

    // ON 절 - 조인 대상 필터링 (ON 절)
    @Test
    public void joinOnFiltering() {
        // 조회 결과 반환
        List<Tuple> result = queryFactory
                                .select(member, team)
                                .from(member)
                                .join(member.team, team)
                                .on(team.name.eq("teamA"))
                                .fetch();
        // 조회 결과 확인
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    // ON 절 - 조인 대상 필터링 (WHERE 절)
    @Test
    public void joinWhereFiltering() {
        // 조회 결과 반환
        List<Tuple> result = queryFactory
                                .select(member, team)
                                .from(member)
                                .join(member.team, team)
                                .where(team.name.eq("teamA"))
                                .fetch();
        // 조회 결과 확인
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    // 연관관계 없는 엔티티 조인 테스트를 위한 데이터 초기화
    public void initForJoinOnNoRelation() {
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));
        entityManager.persist(new Member("teamC"));
        entityManager.flush();
        entityManager.clear();
    }

    // ON 절 - 연관관계 없는 엔티티 조인
    @Test
    public void joinOnNoRelation() {
        // 연관관계 없는 엔티티 조인 테스트를 위한 데이터 초기화
        initForJoinOnNoRelation();
        // 조회 결과 반환
        List<Tuple> result = queryFactory
                                .select(member, team)
                                .from(member)
                                .leftJoin(team)
                                .on(member.username.eq(team.name))
                                .fetch();
        // 조회 결과 확인
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    // 페치 조인 미적용
    @Test
    public void fetchJoinNo() {
        // 조회 결과 반환
        Member result = queryFactory
                                .selectFrom(member)
                                .where(member.username.eq("member1"))
                                .fetchOne();
        // 검증
        boolean isLoaded = entityManagerFactory.getPersistenceUnitUtil().isLoaded(result.getTeam());
        Assertions.assertThat(isLoaded).isFalse();
    }

    // 페치 조인 적용
    @Test
    public void fetchJoin() {
        // 조회 결과 반환
        Member result = queryFactory
                                .selectFrom(member)
                                .join(member.team, team).fetchJoin()
                                .where(member.username.eq("member1"))
                                .fetchOne();
        // 검증
        boolean isLoaded = entityManagerFactory.getPersistenceUnitUtil().isLoaded(result.getTeam());
        Assertions.assertThat(isLoaded).isTrue();
    }

}