package hello.jpql.basic;

import hello.jpql.entity.Member;
import jakarta.persistence.*;

import java.util.List;

public class ParameterBinding {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            entityManager.persist(member);

            // 이름 기반 매개변수 바인딩
            TypedQuery<Member> query1 = entityManager.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class);
            query1.setParameter("name", "member1");

            // 위치 기반 매개변수 바인딩
            TypedQuery<Member> query2 = entityManager.createQuery("SELECT m FROM Member m WHERE m.name = ?1", Member.class);
            query2.setParameter(1, "member1");

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
