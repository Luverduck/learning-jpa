package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}