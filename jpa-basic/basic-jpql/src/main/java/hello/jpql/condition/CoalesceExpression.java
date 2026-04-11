package hello.jpql.condition;

import hello.jpql.entity.Member;
import hello.jpql.entity.MemberType;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class CoalesceExpression {
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

            // COALESCE 함수
            String query = "SELECT COALESCE(m.name, '이름없는 회원') FROM Member m";
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
