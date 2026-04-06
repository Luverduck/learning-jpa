package hello.jpql.basic;

import hello.jpql.entity.Member;
import jakarta.persistence.*;

public class QueryType {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            entityManager.persist(member);

            // Typed Query 사용
            // - JPQL 쿼리 실행 결과 타입이 명확한 경우
            TypedQuery<Member> query1 = entityManager.createQuery("SELECT m FROM Member m", Member.class);
            // Query 사용
            // - JPQL 쿼리 실행 결과 타입이 명확하지 않은 경우
            // - 벌크 연산 (UPDATE, DELETE)
            Query query2 = entityManager.createQuery("SELECT m.name, m.age FROM Member m");

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