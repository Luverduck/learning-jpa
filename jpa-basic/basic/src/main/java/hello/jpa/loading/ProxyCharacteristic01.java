package hello.jpa.loading;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ProxyCharacteristic01 {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 생성
            Member member1 = new Member();
            member1.setName("user1");
            Member member2 = new Member();
            member2.setName("user2");
            // 엔티티 저장
            entityManager.persist(member1);
            entityManager.persist(member2);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 엔티티 조회
            Member refMember = entityManager.getReference(Member.class, member1.getId());
            Member findMember = entityManager.find(Member.class, member2.getId());
            System.out.println("refMember.getClass() = " + refMember.getClass()); // class hello.jpa.entity.Member$HibernateProxy
            System.out.println("findMember.getClass() = " + findMember.getClass()); // class hello.jpa.entity.Member
            System.out.println("refMember instanceof Member = " + (refMember instanceof Member)); // true
            System.out.println("findMember instanceof Member = " + (findMember instanceof Member)); // true
            // 프록시는 엔티티를 상속하므로 getClass()를 통해 동등 비교할 수 없다.
            System.out.println("refMember == findMember = " + (refMember.getClass() == findMember.getClass())); // false
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
