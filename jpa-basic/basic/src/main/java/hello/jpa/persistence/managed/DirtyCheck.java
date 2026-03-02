package hello.jpa.persistence.managed;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class DirtyCheck {
    public static void main(String[] args) {
        // EntityManagerFactory 생성
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        // EntityManager 생성
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // EntityManager를 통해 EntityTransaction 반환
        EntityTransaction transaction = entityManager.getTransaction();
        // 트랜잭션 시작
        transaction.begin();
        try {
            // 객체 생성
            Member member = new Member(201L, "Member");
            // 영속성 컨텍스트에 추가
            entityManager.persist(member);
            // 변경 감지
            member.setName("MemberC");
            member.setName("MemberD");
            // 트랜잭션 커밋
            System.out.println("===  트랜잭션 커밋 전 ===");
            transaction.commit();
            System.out.println("===  트랜잭션 커밋 후 ===");
        } catch (Exception e) {
            // 트랜잭션 롤백
            transaction.rollback();
        } finally {
            // 리소스 정리
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
