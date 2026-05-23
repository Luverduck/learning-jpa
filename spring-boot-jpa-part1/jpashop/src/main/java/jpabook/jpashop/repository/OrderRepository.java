package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    // 주문 저장
    public void save(Order order) {
        entityManager.persist(order);
    }

    // 주문 단일 조회
    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    // 주문 목록 조회
    // - JPQL 문자열 조립
    public List<Order> findAllByString(OrderSearch orderSearch) {
        // JPQL
        String jpql = "select o from Order o join o.member m";
        // 첫 번째 조건인지 여부 > where절을 추가하기 위함
        boolean isFirstCondition = true;
        // 주문 상태 조건
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += "o.status = :status";
        }
        // 회원 이름 조건
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        // 쿼리 객체 생성
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class).setMaxResults(1000);
        // 파라미터 설정
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        // 쿼리 실행
        return query.getResultList();
    }

    // - Criteria API
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        // CriteriaBuilder 반환
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        // CriteriaQuery 생성
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        // FROM 절
        Root<Order> o = cq.from(Order.class);
        // JOIN 절
        Join<Object, Object> m = o.join("member", JoinType.INNER);
        // WHERE 절
        List<Predicate> criteria = new ArrayList<>();
        // 주문 상태 조건
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 조건
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        // 쿼리 객체 생성
        TypedQuery<Order> query = entityManager.createQuery(cq).setMaxResults(1000);
        // 쿼리 실행
        return query.getResultList();
    }

}