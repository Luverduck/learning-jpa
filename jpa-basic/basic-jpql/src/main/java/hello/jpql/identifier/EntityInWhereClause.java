package hello.jpql.identifier;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class EntityInWhereClause {
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
            member.changeTeam(team);
            entityManager.persist(member);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            String query1 = "SELECT m FROM Member m WHERE m = :member";
            List<Member> resultList1 = entityManager.createQuery(query1, Member.class)
                                                    .setParameter("member", member)
                                                    .getResultList();
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            String query2 = "SELECT m FROM Member m WHERE m.team = :team";
            List<Member> resultList2 = entityManager.createQuery(query2, Member.class)
                                                    .setParameter("team", team)
                                                    .getResultList();

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
