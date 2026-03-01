package hello.jpa;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaCreate {
    public static void main(String[] args) {
        // EntityManagerFactory 생성
        // - persistence.xml에서 name이 hello인 <persistence-unit> 항목의 설정을 기반으로 EntityManagerFactory 생성
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        // EntityManager 생성
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // EntityManager를 통해 EntityTransaction 반환
        EntityTransaction transaction = entityManager.getTransaction();
        // 트랜잭션 시작
        transaction.begin();
        try {
            // 회원 저장
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");
            entityManager.persist(member);
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
