package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    // 주문 목록 조회 (DTO 조회)
    public List<OrderQueryDto> findAllDto() {
        // Order 조회
        List<OrderQueryDto> result = findOrders();
        // Order의 id를 통해 OrderItem 조회
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    // Order 조회
    private List<OrderQueryDto> findOrders() {
        return entityManager.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o join o.member m join o.delivery d", OrderQueryDto.class).getResultList();
    }

    // OrderItem 조회
    private List<OrderItemQueryDto> findOrderItems(Long id) {
        return entityManager.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.item.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id = :id", OrderItemQueryDto.class)
                            .setParameter("id", id)
                            .getResultList();
    }

}