package hello.jpa.valuetype;

import hello.jpa.entity.Address;
import hello.jpa.entity.Member;
import hello.jpa.entity.Period;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class EmbeddedType {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 생성
            Member member = new Member();
            member.setName("user1");
            member.setHomeAddress(new Address("city", "street", "10000"));
            member.setWorkPeriod(new Period());
            // 엔티티 저장
            entityManager.persist(member);
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
