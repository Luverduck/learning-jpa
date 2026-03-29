package hello.jpa.valuetype;

import hello.jpa.entity.Address;
import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ValueTypeShare {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // Address 타입 인스턴스 생성
            Address address = new Address("old city", "street", "10000");
            // 엔티티 생성
            Member member1 = new Member();
            member1.setName("member1");
            member1.setHomeAddress(address);
            // 엔티티 저장
            entityManager.persist(member1);
            // 엔티티 생성
            Member member2 = new Member();
            member2.setName("member2");
            member2.setHomeAddress(new Address("new city", address.getStreet(), address.getZipcode()));
            // 엔티티 저장
            entityManager.persist(member2);
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