package hello.jpa.mappping.relation;

import hello.jpa.entity.Member;
import hello.jpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class RelationMapping03 {
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
            member.changeTeam(team); // 연관관계 편의 메소드 호출
            // 팀원 저장
            entityManager.persist(member);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // [연관관계 매핑 주의사항]
            // 3. 양방향 연관관계에서 엔티티 간의 순환참조에 의한 무한루프를 방지해야 한다.
            // toString() 호출
            Member findMember = entityManager.find(Member.class, member.getId());
            System.out.println("m = " + findMember);
            // [연관관계 매핑 주의사항]
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
