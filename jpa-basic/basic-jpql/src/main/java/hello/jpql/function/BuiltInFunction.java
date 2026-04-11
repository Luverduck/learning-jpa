package hello.jpql.function;

import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class BuiltInFunction {
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
            member.setName(" member1 ");
            member.setAge(10);
            member.changeTeam(team);
            entityManager.persist(member);

            // 1) 문자 함수
            // CONCAT
            String query1 = "SELECT CONCAT(m.name, m.team.name) FROM Member m";
            List<Object[]> resultList1 = entityManager.createQuery(query1, Object[].class).getResultList();
            // SUBSTRING
            String query2 = "SELECT SUBSTRING(m.name, 1, 3) FROM Member m";
            List<Object[]> resultList2 = entityManager.createQuery(query2, Object[].class).getResultList();
            // TRIM
            String query3 = "SELECT TRIM(m.name) FROM Member m";
            List<Object[]> resultList3 = entityManager.createQuery(query3, Object[].class).getResultList();
            // UPPER
            String query4 = "SELECT UPPER(m.name) FROM Member m";
            List<Object[]> resultList4 = entityManager.createQuery(query4, Object[].class).getResultList();
            // LENGTH
            String query5 = "SELECT LENGTH(m.name) FROM Member m";
            List<Object[]> resultList5 = entityManager.createQuery(query5, Object[].class).getResultList();
            // LOCATE
            String query6 = "SELECT LOCATE(m.name, 'ber') FROM Member m";
            List<Object[]> resultList6 = entityManager.createQuery(query6, Object[].class).getResultList();

            // 2) 수학 함수
            // SQRT
            String query7 = "SELECT SQRT(m.age) FROM Member m";
            List<Object[]> resultList7 = entityManager.createQuery(query7, Object[].class).getResultList();
            // MOD
            String query8 = "SELECT MOD(m.age / 3) FROM Member m";
            List<Object[]> resultList8 = entityManager.createQuery(query8, Object[].class).getResultList();

            // 3) 컬렉션 함수
            // SIZE
            String query9 = "SELECT SIZE(t.members) FROM Team t";
            List<Object[]> resultList9 = entityManager.createQuery(query9, Object[].class).getResultList();

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
