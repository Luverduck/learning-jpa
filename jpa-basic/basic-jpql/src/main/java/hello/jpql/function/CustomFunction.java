package hello.jpql.function;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class CustomFunction {
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
            for (int i = 0; i < 3; ++i) {
                Member member = new Member();
                member.setName("member" + i);
                member.setAge(10);
                member.changeTeam(team);
                entityManager.persist(member);
            }

            // 사용자 정의 함수
            String query = "SELECT GROUP_CONCAT(m.name) FROM Member m";
            List<Object[]> resultList = entityManager.createQuery(query, Object[].class).getResultList();
            for (Object[] result : resultList) {
                System.out.println("result = " + result[0]);
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
