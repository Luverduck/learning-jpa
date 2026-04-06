package hello.jpql.projection;

import hello.jpql.entity.Address;
import hello.jpql.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class EmbeddedTypeProjection {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Order order = new Order();
            order.setOrderAmount(10);
            order.setAddress(new Address("new city", "new street", "new zipcode"));
            entityManager.persist(order);

            // 임베디드 타입 프로젝션
            List<Address> resultList = entityManager.createQuery("SELECT o.address FROM Order o", Address.class).getResultList();
            for (Address result : resultList) {
                System.out.println("result = " + result.getCity() + " / " + result.getStreet() + " / " + result.getZipcode());
            }

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
