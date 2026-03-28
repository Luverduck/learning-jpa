package hello.jpa.loading.fetch;

import hello.jpa.entity.Member;
import hello.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class EagerLoadingProblem {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            for (int i = 1; i <= 3; ++i) {
                // 엔티티 저장
                Team team = new Team();
                team.setName("team" + i);
                entityManager.persist(team);
                // 엔티티 저장
                Member member = new Member();
                member.setName("user" + i);
                member.setTeam(team);
                entityManager.persist(member);
            }
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            // Member 엔티티 조회 결과만큼 Team 엔티티 조회 쿼리가 추가로 실행
            List<Member> members = entityManager.createQuery("select m from Member m", Member.class).getResultList();
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
