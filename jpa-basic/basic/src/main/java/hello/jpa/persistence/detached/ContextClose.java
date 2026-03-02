package hello.jpa.persistence.detached;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ContextClose {
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
            // 엔티티 조회 1회차
            System.out.println("=== 엔티티 조회 1회차 ===");
            Member findMember1 = entityManager.find(Member.class, 1L);
            // 엔티티 상태 변경
            System.out.println("=== 엔티티 상태 변경 ===");
            findMember1.setName("MemberA");
            // 영속성 컨텍스트 종료
            System.out.println("=== 영속성 컨텍스트 종료 ===");
            entityManager.close();
            // 트랜잭션 커밋
            System.out.println("=== 트랜잭션 커밋 ===");
            transaction.commit();
        } catch (Exception e) {
            // 트랜잭션 롤백
            transaction.rollback();
        }
        entityManagerFactory.close();
    }
}
