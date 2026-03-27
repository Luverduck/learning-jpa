package hello.jpa.loading;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ProxyBasic {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 생성
            Member member = new Member();
            member.setName("user1");
            // 엔티티 저장
            entityManager.persist(member);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            // Member findMember = entityManager.find(Member.class, member.getId());
            // System.out.println("findMember.getId() = " + findMember.getId());
            // System.out.println("findMember.getName() = " + findMember.getName());
            Member refMember = entityManager.getReference(Member.class, member.getId());
            System.out.println("refMember.getId() = " + refMember.getId());
            System.out.println("refMember.getName() = " + refMember.getName());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}