package springdatajpa.basic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import springdatajpa.basic.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // 저장
    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    // 삭제
    public void delete(Member member) {
        entityManager.remove(member);
    }

    // 전체 조회
    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 단일 조회
    public Optional<Member> findById(Long id) {
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // 전체 개수 조회
    public Long count() {
        return entityManager.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    // 쿼리 메소드
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return entityManager.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
                            .setParameter("username", username)
                            .setParameter("age", age)
                            .getResultList();
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

}