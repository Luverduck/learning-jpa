package hello.jpa.mappping.relation;

import hello.jpa.entity.Member;
import hello.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class UndirectionalRelation {
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
            Member findMember1 = entityManager.find(Member.class, member.getId());
            System.out.println("findMember1 = " + findMember1.getName());
            Team findTeam1 = findMember1.getTeam();
            System.out.println("findTeam1 = " + findTeam1.getName());
            // 새로운 팀 생성
            Team teamB = new Team();
            teamB.setName("TeamB");
            entityManager.persist(teamB);
            // 팀원 수정
            findMember1.setTeam(teamB);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 팀이 변경된 팀원 조회
            Member findMember2 = entityManager.find(Member.class, findMember1.getId());
            System.out.println("findMember2 = " + findMember2.getName());
            Team findTeam2 = findMember2.getTeam();
            System.out.println("findTeam2 = " + findTeam2.getName());
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
