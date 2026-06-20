package springdatajpa.basic.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import springdatajpa.basic.entity.Member;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager entityManager;

    // 쿼리 메소드 구현 (사용자 정의 리포지토리)
    @Override
    public List<Member> findMemberCustom() {
        System.out.println("MemberRepositoryCustomImpl.findMemberCustom()");
        return entityManager.createQuery("select m from Member m", Member.class).getResultList();
    }

}