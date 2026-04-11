package hello.jpql.pathepression;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class CollectionValued {
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
            for (int i = 1; i <= 3; ++i) {
                Member member = new Member();
                member.setName("member" + i);
                member.setAge(i);
                member.changeTeam(team);
                entityManager.persist(member);
            }

            // 컬렉션 값 경로 표현식
            String query1 = "SELECT t.members FROM Team t";
            List<Member> resultList1 = entityManager.createQuery(query1, Member.class).getResultList();

            String query2 = "SELECT m.name FROM Team t INNER JOIN t.members m";
            List<String> resultList2 = entityManager.createQuery(query2, String.class).getResultList();
            for (String result : resultList2) {
                System.out.println("result = " + result);
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
