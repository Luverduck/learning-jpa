package querydsl.basic;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.basic.entity.Hello;
import querydsl.basic.entity.QHello;

@SpringBootTest
@Transactional
class BasicApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {
        // Hello 엔티티 저장
        Hello hello = new Hello();
        entityManager.persist(hello);
        // Querydsl 쿼리에서 Hello 엔티티를 "h"라는 별칭으로 표현하는 Q 타입 생성
        QHello qHello = new QHello("h");
        // Querydsl 쿼리 생성기 생성
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        // Q 타입을 통해 Hello 엔티티 조회
        Hello result = queryFactory.selectFrom(qHello).fetchOne();
        // 검증
        Assertions.assertThat(result).isEqualTo(hello);
        Assertions.assertThat(result.getId()).isEqualTo(hello.getId());
    }

}