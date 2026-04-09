package hello.jpql.subquery;

import hello.jpql.entity.Address;
import hello.jpql.entity.Member;
import hello.jpql.entity.Order;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class SubQuery {
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

            // 엔티티 조회 (서브 쿼리)
            // 1) WHERE 절
            String query1 = "SELECT m FROM Member m WHERE m.age > (SELECT AVG(m2.age) FROM Member m2)";
            List<Member> resultList1 = entityManager.createQuery(query1, Member.class).getResultList();
            // 2) HAVING 절
            String query2 = "SELECT m.team, AVG(m.age) FROM Member m GROUP BY m.team HAVING AVG(m.age) > (SELECT AVG(m2.age) FROM Member m2)";
            List<Object[]> resultList2 = entityManager.createQuery(query2, Object[].class).getResultList();
            // 3) SELECT 절 (Hibernate에서 지원)
            String query3 = "SELECT (SELECT AVG(m1.age) FROM Member m1) AS avgAge FROM Member m";
            List<Object[]> resultList3 = entityManager.createQuery(query3, Object[].class).getResultList();

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
