package hello.jpa.flush;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpqlExecutionFlush {
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
            // 회원 생성
            Member memberA = new Member(301L, "MemberA");
            Member memberB = new Member(302L, "MemberB");
            Member memberC = new Member(303L, "MemberC");
            // 영속성 컨텍스트에 저장
            entityManager.persist(memberA);
            entityManager.persist(memberB);
            entityManager.persist(memberC);
            // JPQL 실행
            System.out.println("=== JPQL 실행 전 ===");
            List<Member> result = entityManager.createQuery("SELECT m FROM Member AS m", Member.class)
                    .getResultList();
            System.out.println("=== JPQL 실행 후 ===");
            // 트랜잭션 커밋
            System.out.println("=== 트랜잭션 커밋 전 ===");
            transaction.commit();
            System.out.println("=== 트랜잭션 커밋 후 ===");
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
