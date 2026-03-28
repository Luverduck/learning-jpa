package hello.jpa.loading.cascade;

import hello.jpa.entity.Child;
import hello.jpa.entity.Parent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class OrphanRemoval {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 자식 엔티티 생성
            Child child1 = new Child();
            Child child2 = new Child();
            // 부모 엔티티 생성
            Parent parent = new Parent();
            // 부모 엔티티의 컬렉션 필드에 자식 엔티티 추가
            parent.addChild(child1);
            parent.addChild(child2);
            // 부모 엔티티를 영속성 컨텍스트에 등록
            entityManager.persist(parent);
            // 영속성 컨텍스트 비우기
            entityManager.flush();
            entityManager.clear();
            // 부모 엔티티의 컬렉션 필드에서 자식 엔티티 제거
            Parent findParent = entityManager.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);
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