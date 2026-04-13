package hello.jpql.fetchjoin;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class FetchJoinBasic {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Team teamA = new Team();
            teamA.setName("teamA");
            entityManager.persist(teamA);
            for (int i = 1; i <= 2; ++i) {
                Member member = new Member();
                member.setName("member" + i);
                member.setAge(i);
                member.changeTeam(teamA);
                entityManager.persist(member);
            }
            Team teamB = new Team();
            teamB.setName("teamB");
            entityManager.persist(teamB);
            Member member = new Member();
            member.setName("member3");
            member.setAge(10);
            member.changeTeam(teamB);
            entityManager.persist(member);
            // 엔티티 저장
            entityManager.persist(member);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 일반 조회
            // String query = "SELECT m FROM Member m";
            // 페치 조인
            String query = "SELECT m FROM Member m JOIN FETCH m.team";
            List<Member> resultList = entityManager.createQuery(query, Member.class).getResultList();
            for (Member result : resultList) {
                System.out.println("result = " + result.getName() + ", " + result.getTeam().getName());
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
