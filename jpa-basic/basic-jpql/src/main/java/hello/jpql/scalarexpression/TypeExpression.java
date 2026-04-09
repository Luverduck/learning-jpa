package hello.jpql.scalarexpression;

import hello.jpql.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class TypeExpression {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 엔티티 저장
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");
            entityManager.persist(book);

            // 엔티티 조회
            // TYPE 표현식
            String query = "SELECT i FROM Item i WHERE TYPE(i) = Book";
            List<Object[]> resultList1 = entityManager.createQuery(query, Object[].class).getResultList();

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
