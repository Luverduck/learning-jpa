package hello.jpql.condition;

import hello.jpql.entity.Member;
import hello.jpql.entity.MemberType;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class GeneralCaseExpression {
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
            member.setType(MemberType.ADMIN);
            member.changeTeam(team);
            entityManager.persist(member);

            // 일반 CASE 표현식
            String query = "SELECT " +
                                "CASE " +
                                "WHEN m.age <= 10 THEN '학생요금' " +
                                "when m.age >= 60 THEN '경로요금' " +
                                "ELSE '일반요금' " +
                                "END " +
                            "FROM Member m";
            List<Object[]> resultList = entityManager.createQuery(query, Object[].class).getResultList();

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
