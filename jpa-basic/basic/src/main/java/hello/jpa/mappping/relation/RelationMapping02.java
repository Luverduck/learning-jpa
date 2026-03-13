package hello.jpa.mappping.relation;

import hello.jpa.entity.Member;
import hello.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class RelationMapping02 {
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
            // [연관관계 매핑 주의사항 2]
            // member.setTeam(team); // 팀원의 팀 설정
            member.changeTeam(team); // 연관관계 편의 메소드 호출
            // [연관관계 매핑 주의사항 2]
            // 팀원 저장
            entityManager.persist(member);
            // 출력
            Team findTeam = entityManager.find(Team.class, team.getId());
            List<Member> findTeamMembers = findTeam.getMembers();
            System.out.println("=====================");
            for (Member m : findTeamMembers) {
                System.out.println("m = " + m.getName());
            }
            System.out.println("=====================");
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
