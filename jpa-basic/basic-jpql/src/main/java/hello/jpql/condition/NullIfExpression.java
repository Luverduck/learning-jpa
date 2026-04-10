package hello.jpql.condition;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class NullIfExpression {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Team team = new Team();
            team.setName("teamA");
            entityManager.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            member.changeTeam(team);
            entityManager.persist(member);

            // 엔티티 조회
            // 리터럴
            // - 문자
            String query1 = "SELECT m FROM Member m WHERE m.name = 'member1'";
            List<Object[]> resultList1 = entityManager.createQuery(query1, Object[].class).getResultList();
            // - 숫자
            String query2 = "SELECT m FROM Member m WHERE m.age = 10L";
            List<Object[]> resultList2 = entityManager.createQuery(query2, Object[].class).getResultList();
            // - 열거
            String query4 = "SELECT m FROM Member m WHERE m.type = hello.jpql.entity.MemberType.ADMIN";
            List<Object[]> resultList4 = entityManager.createQuery(query4, Object[].class).getResultList();

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
