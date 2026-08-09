package querydsl.basic.repository.support;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public abstract class QuerydslRepositorySupportCustom {

    // 필드
    private final PathBuilder<?> builder;
    private @Nullable EntityManager entityManager;
    private @Nullable Querydsl querydsl;
    private @Nullable JPAQueryFactory queryFactory;

    // 생성자
    protected QuerydslRepositorySupportCustom(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null");
        this.builder = new PathBuilderFactory().create(domainClass);
    }

    // EntityManager 주입 및 Querydsl, JPAQueryFactory 초기화
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null");
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, builder);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 의존성 주입 완료 후 초기화 상태 검증
    @PostConstruct
    public void validate() {
        getEntityManager();
        getQuerydsl();
        getQueryFactory();
    }

    // EntityManager 반환
    protected final EntityManager getEntityManager() {
        EntityManager entityManager = this.entityManager;
        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is null");
        }
        return entityManager;
    }

    // Querydsl 반환
    protected final Querydsl getQuerydsl() {
        Querydsl querydsl = this.querydsl;
        if (querydsl == null) {
            throw new IllegalStateException("Querydsl is null");
        }
        return querydsl;
    }

    // JPAQueryFactory 반환 - select(), selectFrom() 사용을 위함
    protected final JPAQueryFactory getQueryFactory() {
        JPAQueryFactory queryFactory = this.queryFactory;
        if (queryFactory == null) {
            throw new IllegalStateException("JPAQueryFactory is null");
        }
        return queryFactory;
    }

    // 지정한 표현식을 조회하는 select 쿼리 생성
    protected final <T> JPAQuery<T> select(Expression<T> expression) {
        Assert.notNull(expression, "Expression must not be null");
        return getQueryFactory().select(expression);
    }

    // 지정한 엔티티를 조회하는 select-from 쿼리 생성
    protected final <T> JPAQuery<T> selectFrom(EntityPath<T> path) {
        Assert.notNull(path, "EntityPath must not be null");
        return getQueryFactory().selectFrom(path);
    }

    // 페이징, 정렬을 적용하고 count 쿼리를 이용해 Page 생성
    protected final <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPQLQuery<T>> contentQuery, Function<JPAQueryFactory, JPQLQuery<Long>> countQuery) {
        Assert.notNull(pageable, "Pageable must not be null");
        Assert.notNull(contentQuery, "Content query must not be null");
        Assert.notNull(countQuery, "Count query must not be null");
        // content 조회 쿼리 생성
        JPAQueryFactory queryFactory = getQueryFactory();
        JPQLQuery<T> contentJpqlQuery = contentQuery.apply(queryFactory);
        Assert.notNull(contentJpqlQuery, "Content query result must not be null");
        // Pageable 적용 후 쿼리 실행
        List<T> content = getQuerydsl()
                .applyPagination(pageable, contentJpqlQuery)
                .fetch();
        // content를 이용해 Page 생성하고 필요한 경우 count 쿼리 실행
        return PageableExecutionUtils.getPage(content, pageable, () -> {
            JPQLQuery<Long> countJpqlQuery = countQuery.apply(queryFactory);
            Assert.notNull(countJpqlQuery, "Count query result must not be null");
            return Optional.ofNullable(countJpqlQuery.fetchOne()).orElse(0L);
        });
    }

}
