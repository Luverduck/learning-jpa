package hello.jpa.mappping.relation;

import hello.jpa.entity.Member;
import hello.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class BidirectionalRelation {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 팀 생성
            Team teamA = new Team();
            teamA.setName("TeamA");
            entityManager.persist(teamA);
            // 팀원 생성
            Member member = new Member();
            member.setName("Member1");
            member.setTeam(teamA); // 팀원의 팀 설정
            // 팀원 저장
            entityManager.persist(member);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 팀원 조회
            Member findMember = entityManager.find(Member.class, member.getId());
            // 팀원의 팀을 통해 해당 팀에 속한 모든 팀원 조회
            List<Member> findMembers = findMember.getTeam().getMembers();
            for (var m : findMembers) {
                System.out.println("member = " + m.getName());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}