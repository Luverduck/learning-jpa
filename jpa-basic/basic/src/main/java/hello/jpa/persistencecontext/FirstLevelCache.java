package hello.jpa.persistencecontext;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class FirstLevelCache {
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
            // 애플리케이션에서 식별자를 통해 엔티티를 조회할 때 가장 먼저 1차 캐시에서 조회
            System.out.println("=== 식별자가 같은 엔티티 조회 ===");
            // - 1차 캐시에 해당 식별자의 엔티티가 존재할 경우 해당 엔티티를 즉시 반환
            System.out.println("=== 1회 조회 ===");
            Member findMember1 = entityManager.find(Member.class, 1L);
            System.out.println("=== 2회 조회 ===");
            Member findMember2 = entityManager.find(Member.class, 1L);
            System.out.println("=== 식별자가 다른 엔티티 조회 ===");
            // - 1차 캐시에 해당 식별자의 엔티티가 존재하지 않을 경우
            Member findMember3 = entityManager.find(Member.class, 2L);
            // 엔티티의 동일성(Identity) 보장
            System.out.println("findMember1 == findMember2 : " + (findMember1 == findMember2));
            // 트랜잭션 커밋
            transaction.commit();
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
