package hello.jpql.projection;

import hello.jpql.dto.MemberDTO;
import hello.jpql.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class ScalarTypeProjection {
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

            // 스칼라 타입 프로젝션
            // - 단일 스칼라 타입
            List<String> resultList1 = entityManager.createQuery("SELECT m.name FROM Member m", String.class).getResultList();
            for (String result1 : resultList1) {
                System.out.println("result1 = " + result1);
            }
            // - 복합 스칼라 타입
            List<Object[]> resultList2 = entityManager.createQuery("SELECT m.name, m.age FROM Member m", Object[].class).getResultList();
            for (Object[] result2 : resultList2) {
                System.out.println("result2 = " + result2[0] + " / " + result2[1]);
            }
            // - 복합 스칼라 타입 >> Query 방식
            List resultList3 = entityManager.createQuery("SELECT m.name, m.age FROM Member m").getResultList();
            for (Object result3 : resultList3) {
                Object[] objs = (Object[]) result3;
                System.out.println("result3 = " + objs[0] + " / " + objs[1]);
            }
            // - 복합 스칼라 타입 (DTO 방식)
            List<MemberDTO> resultList4 = entityManager.createQuery("SELECT new hello.jpql.dto.MemberDTO(m.name, m.age) FROM Member m").getResultList();
            for (MemberDTO result4 : resultList4) {
                System.out.println("result4 = " + result4.getName() + " / " + result4.getAge());
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
