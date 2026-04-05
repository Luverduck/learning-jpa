package hello.jpa.query.intro;

import hello.jpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class CriteriaApiBasic {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 쿼리 빌더 반환
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            // 쿼리 객체 생성
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
            // 조회 대상 설정
            Root<Member> m = query.from(Member.class);
            // 쿼리 정의
            CriteriaQuery<Member> criteriaQuery = query.select(m).where(criteriaBuilder.equal(m.get("name"), "kim"));
            // 쿼리 실행
            List<Member> result = entityManager.createQuery(criteriaQuery).getResultList();

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