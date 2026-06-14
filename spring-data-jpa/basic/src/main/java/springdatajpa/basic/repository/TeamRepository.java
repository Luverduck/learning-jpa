package springdatajpa.basic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import springdatajpa.basic.entity.Member;
import springdatajpa.basic.entity.Team;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // 저장
    public Team save(Team team) {
        entityManager.persist(team);
        return team;
    }

    // 삭제
    public void delete(Team team) {
        entityManager.remove(team);
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

}