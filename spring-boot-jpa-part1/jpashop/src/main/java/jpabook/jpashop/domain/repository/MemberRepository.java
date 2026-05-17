package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // EntityManager 주입
    private final EntityManager entityManager;

    // 회원 저장
    public void save(Member member) {
        entityManager.persist(member);
    }

    // 회원 단일 조회
    public Member findOne(Long id) {
        return entityManager.find(Member.class, id);
    }

    // 회원 목록 조회
    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 회원 목록 조회 (조건)
    public List<Member> findByName(String name) {
        return entityManager.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}