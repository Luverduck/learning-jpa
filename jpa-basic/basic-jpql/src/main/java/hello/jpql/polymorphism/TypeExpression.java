package hello.jpql.polymorphism;

import hello.jpql.entity.Item;
import hello.jpql.entity.Member;
import hello.jpql.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class TypeExpression {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            for (int i = 0; i < 14; ++i) {
                Team team = new Team();
                team.setName("team" + (char)('A' + i));
                entityManager.persist(team);
                for (int j = 0; j < 3; ++j) {
                    Member member = new Member();
                    member.setName("member" + (char)('A' + i) + j);
                    member.setAge(i);
                    member.changeTeam(team);
                    entityManager.persist(member);
                }
            }
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            String query = "SELECT i FROM Item i WHERE TYPE(i) IN (Book, Movie)";
            List<Item> resultList = entityManager.createQuery(query, Item.class).getResultList();

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
