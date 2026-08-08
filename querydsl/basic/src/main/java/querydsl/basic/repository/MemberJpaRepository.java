package querydsl.basic.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import querydsl.basic.dto.*;
import querydsl.basic.entity.Member;

import java.util.List;
import java.util.Optional;

import static querydsl.basic.entity.QMember.member;
import static querydsl.basic.entity.QTeam.team;

@Repository
public class MemberJpaRepository {

    // 필드
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    // 생성자
    public MemberJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 엔티티 저장
    public void save(Member member) {
        entityManager.persist(member);
    }

    // 엔티티 단일 조회
    public Optional<Member> findById(Long id) {
        Member findMember = entityManager.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    // 엔티티 전체 조회 - 순수 JPA
    public List<Member> findAll() {
        return entityManager
                    .createQuery("select m from Member m", Member.class)
                    .getResultList();
    }

    // 엔티티 조건 조회 - 순수 JPA
    public List<Member> findByUsername(String username) {
        return entityManager
                    .createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", username)
                    .getResultList();
    }

    // 엔티티 전체 조회 - QueryDSL
    public List<Member> findAllQuerydsl() {
        return queryFactory
                    .selectFrom(member)
                    .fetch();
    }

    // 엔티티 조건 조회 - QueryDSL
    public List<Member> findByUsernameQuerydsl(String username) {
        return queryFactory
                    .selectFrom(member)
                    .where(member.username.eq(username))
                    .fetch();
    }

    // DTO 조회 - Projections
    public List<MemberDto> searchDtoByProjections() {
        return queryFactory
                    .select(
                        Projections.constructor(MemberDto.class, member.username, member.age)
                    )
                    .from(member)
                    .fetch();
    }

    // DTO 조회 - QueryProjection
    public List<MemberDto> searchDtoByQueryProjection() {
        return queryFactory
                    .select(
                        new QMemberDto(member.username, member.age)
                    )
                    .from(member)
                    .fetch();
    }

    // 동적 쿼리 - BooleanBuilder 방식
    public List<MemberTeamDto> searchByBooleanBuilder(MemberSearchCondition condition) {
        // BooleanBuilder 구성
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeMin() != null) {
            builder.and(member.age.goe(condition.getAgeMin()));
        }
        if (condition.getAgeMax() != null) {
            builder.and(member.age.loe(condition.getAgeMax()));
        }
        // 조건 조회
        return queryFactory
                    .select(
                        new QMemberTeamDto(
                            member.id.as("memberId"),
                            member.username,
                            member.age,
                            team.id.as("teamId"),
                            team.name.as("teamName")
                        )
                    )
                    .from(member)
                    .leftJoin(member.team, team)
                    .where(builder)
                    .fetch();
    }

    // 동적 쿼리 - where() 다중 파라미터 방식
    public List<MemberTeamDto> searchByWhereMultiParameter(MemberSearchCondition condition) {
        // 조건 조회
        return queryFactory
                    .select(
                            new QMemberTeamDto(
                                    member.id.as("memberId"),
                                    member.username,
                                    member.age,
                                    team.id.as("teamId"),
                                    team.name.as("teamName")
                            )
                    )
                    .from(member)
                    .leftJoin(member.team, team)
                    .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeMin()),
                        ageLoe(condition.getAgeMax())
                    )
                    .fetch();
    }

    // 사용자 이름 조건
    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    // 팀 이름 조건
    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    // 사용자 나이 하한 조건
    private BooleanExpression ageGoe(Integer ageMin) {
        return ageMin != null ? member.age.goe(ageMin) : null;
    }

    // 사용자 나이 상한 조건
    private BooleanExpression ageLoe(Integer ageMax) {
        return ageMax != null ? member.age.goe(ageMax) : null;
    }

}
