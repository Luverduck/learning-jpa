package hello.jpql.namedquery;

import hello.jpql.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class NamedQueryXml {
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
            Member member3 = new Member();
            member3.setName("member3");
            member3.setAge(30);
            entityManager.persist(member3);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();

            // Named 쿼리 실행
            List<Member> findMembers = entityManager.createNamedQuery("Member.findByAge", Member.class)
                    .setParameter("age", member2.getAge())
                    .getResultList();
            // Named 쿼리 실행 결과 출력
            for (Member findMember : findMembers) {
                System.out.println("findMember = " + findMember.getName() + ", " + findMember.getAge());
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
