package hello.jpa.query.intro;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.jpa.entity.Member;
import hello.jpa.entity.QMember;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class QuerydslBasic {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            // 쿼리 팩토리 생성
            JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
            // JPQL 쿼리 생성 및 실행
            QMember m = QMember.member;
            List<Member> result = queryFactory.select(m).from(m).where(m.name.like("kim")).orderBy(m.id.desc()).fetch();

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
