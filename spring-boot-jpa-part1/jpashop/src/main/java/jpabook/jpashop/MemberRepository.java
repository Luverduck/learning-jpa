package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    // 현재 실행 중인 트랜잭션에 바인딩된 EntityManager 주입
    @PersistenceContext
    private EntityManager entityManager;

    // 회원 저장
    public Long save(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    // 회원 조회
    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

}