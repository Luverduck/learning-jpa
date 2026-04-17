package hello.jpql.bulkoperation;

import hello.jpql.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class BulkOperation {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Member member1 = new Member();
            member1.setName("member1");
            member1.setAge(8);
            entityManager.persist(member1);
            Member member2 = new Member();
            member2.setName("member2");
            member2.setAge(11);
            entityManager.persist(member2);
            Member member3 = new Member();
            member3.setName("member1");
            member3.setAge(29);
            entityManager.persist(member3);

            // 벌크 연산
            String query = "UPDATE Member m SET m.age = 20";
            int result = entityManager.createQuery(query).executeUpdate();
            // Query의 executeUpdate()는 영향받은 데이터 행 수를 반환한다.
            System.out.println("result = " + result);

            // 영속성 컨텍스트를 비우기 전
            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("findMember1 = " + findMember1.getName() + ", " + findMember1.getAge());

            // 영속성 컨텍스트 비우기
            entityManager.clear();

            // 영속성 컨텍스트를 비운 후
            findMember1 = entityManager.find(Member.class, member1.getId());
            entityManager.find(Member.class, member1.getId());
            System.out.println("findMember1 = " + findMember1.getName() + ", " + findMember1.getAge());

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
