package querydsl.basic.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import querydsl.basic.entity.Member;

import java.util.List;
import java.util.Optional;

import static querydsl.basic.entity.QMember.member;

@Repository
public class MemberJpaRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public void save(Member member) {
        entityManager.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = entityManager.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<Member> findAll() {
        return entityManager
                    .createQuery("select m from Member m", Member.class)
                    .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return entityManager
                    .createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", username)
                    .getResultList();
    }

    public List<Member> findAllQuerydsl() {
        return queryFactory
                    .selectFrom(member)
                    .fetch();
    }

    public List<Member> findByUsernameQuerydsl(String username) {
        return queryFactory
                    .selectFrom(member)
                    .where(member.username.eq(username))
                    .fetch();
    }

}
