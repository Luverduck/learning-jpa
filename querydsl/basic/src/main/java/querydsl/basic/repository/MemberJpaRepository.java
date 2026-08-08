package querydsl.basic.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import querydsl.basic.dto.MemberDto;
import querydsl.basic.dto.QMemberDto;
import querydsl.basic.entity.Member;

import java.util.List;
import java.util.Optional;

import static querydsl.basic.entity.QMember.member;

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

}
