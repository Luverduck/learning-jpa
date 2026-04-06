package hello.jpql.pagination;

import hello.jpql.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class Pagination {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            for (int i = 1; i <= 100; ++i) {
                Member member = new Member();
                member.setName("member" + i);
                member.setAge(i);
                entityManager.persist(member);
            }

            // 엔티티 조회 (페이지네이션)
            List<Member> resultList = entityManager.createQuery("SELECT m FROM Member m", Member.class)
                                                    .setFirstResult(10) // 10번부터
                                                    .setMaxResults(10)  // 10개
                                                    .getResultList();   // 조회
            for (Member result : resultList) {
                System.out.println("result = " + result.getName() + " / " + result.getAge());
            }

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
