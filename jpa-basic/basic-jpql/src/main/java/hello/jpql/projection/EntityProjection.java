package hello.jpql.projection;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.*;

import java.util.List;

public class EntityProjection {
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

            entityManager.flush();
            entityManager.clear();

            // 엔티티 프로젝션
            List<Member> resultList = entityManager.createQuery("SELECT m FROM Member m", Member.class).getResultList();

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