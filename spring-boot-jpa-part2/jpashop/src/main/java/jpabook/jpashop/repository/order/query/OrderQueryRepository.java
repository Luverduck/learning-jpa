package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    // 주문 목록 조회 (DTO 조회)
    public List<OrderQueryDto> findAllDto() {
        // Order 조회 후 그 결과를 OrderQueryDto로 반환
        List<OrderQueryDto> result = findOrders();
        // List<OrderQueryDto>를 순회하며 OrderItem 조회 후 그 결과를 OrderItemQueryDto로 반환
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    // 주문 목록 조회 (DTO 조회 - 컬렉션 조회 최적화)
    public List<OrderQueryDto> findAllDtoOptimization() {
        // Order 조회 후 그 결과를 OrderQueryDto로 반환
        List<OrderQueryDto> result = findOrders();
        // Order 조회 결과에서 Order의 id를 List 형태로 반환
        List<Long> orderIds = result.stream().map(o -> o.getId()).toList();
        // OrderItem 조회 후 그 결과를 OrderItemQueryDto로 반환 (in 절을 통한 일괄 조회)
        List<OrderItemQueryDto> orderItems = entityManager.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id in :orderIds", OrderItemQueryDto.class)
                                                            .setParameter("orderIds", orderIds)
                                                            .getResultList();
        // OrderItemQueryDto를 id 별로 그룹화하여 Map 형태로 반환
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getId()));
        // List<OrderQueryDto>를 순회하며 Order의 id에 해당하는 List<OrderItemQueryDto> 할당
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));
        return result;
    }

    // 주문 목록 조회 (DTO 조회 - 플랫 데이터 최적화)
    public List<OrderFlatDto> findAllDtoFlat() {
        return entityManager.createQuery("select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) from Order o join o.member m join o.delivery d join o.orderItems oi join oi.item i", OrderFlatDto.class).getResultList();
    }

    // Order 조회
    private List<OrderQueryDto> findOrders() {
        return entityManager.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o join o.member m join o.delivery d", OrderQueryDto.class).getResultList();
    }

    // OrderItem 조회
    private List<OrderItemQueryDto> findOrderItems(Long id) {
        return entityManager.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id = :id", OrderItemQueryDto.class)
                            .setParameter("id", id)
                            .getResultList();
    }

}