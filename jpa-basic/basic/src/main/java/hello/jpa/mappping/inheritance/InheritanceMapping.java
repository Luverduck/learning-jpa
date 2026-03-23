package hello.jpa.mappping.inheritance;

import hello.jpa.entity.Member;
import hello.jpa.entity.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class InheritanceMapping {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Movie movie = new Movie();
            movie.setName("바람과 함께 사라지다.");
            movie.setDirector("DIRECTOR");
            movie.setActor("ACTOR");
            movie.setPrice(10000);
            entityManager.persist(movie);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
