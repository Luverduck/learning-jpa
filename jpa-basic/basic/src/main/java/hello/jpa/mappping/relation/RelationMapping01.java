package hello.jpa.mappping.relation;

import hello.jpa.entity.Member;
import hello.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class RelationMapping01 {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 팀 생성
            Team team = new Team();
            team.setName("TeamA");
            entityManager.persist(team);
            // 팀원 생성
            Member member = new Member();
            member.setName("Member1");
            // [연관관계 매핑 주의사항 1]
            member.setTeam(team); // 팀원의 팀 설정
            // 팀에 팀원 추가
            // team.getMembers().add(member);
            // [연관관계 매핑 주의사항 1]
            // 팀원 저장
            entityManager.persist(member);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
