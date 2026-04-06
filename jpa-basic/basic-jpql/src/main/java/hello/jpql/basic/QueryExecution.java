package hello.jpql.basic;

import hello.jpql.entity.Member;
import jakarta.persistence.*;

import java.util.List;

public class QueryExecution {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Member member1 = new Member();
            member1.setName("member1");
            member1.setAge(10);
            entityManager.persist(member1);
            Member member2 = new Member();
            member2.setName("member2");
            member2.setAge(20);
            entityManager.persist(member2);

            // 쿼리 생성
            TypedQuery<Member> query = entityManager.createQuery("SELECT m FROM Member m", Member.class);

            // 쿼리 실행 결과 반환
            List<Member> resultList = query.getResultList();
            for (Member member : resultList) {
                System.out.println("member = " + member.getName() + " " + member.getAge());
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
