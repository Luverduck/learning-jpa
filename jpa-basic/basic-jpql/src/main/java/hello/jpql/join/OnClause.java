package hello.jpql.join;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class OnClause {
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
            // - 조인 대상 필터링
            String query1 = "SELECT m, t FROM Member m LEFT OUTER JOIN m.team t ON t.name = 'A'";
            List<Object[]> resultList1 = entityManager.createQuery(query1, Object[].class).getResultList();
            // - 연관관계가 없는 엔티티 간의 조인 조건 명시
            String query2 = "SELECT m, t FROM Member m LEFT OUTER JOIN Team t ON m.name = t.name";
            List<Object[]> resultList2 = entityManager.createQuery(query2, Object[].class).getResultList();

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
