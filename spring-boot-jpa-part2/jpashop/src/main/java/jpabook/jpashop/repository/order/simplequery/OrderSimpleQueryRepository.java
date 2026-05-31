package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager entityManager;

    // 주문 목록 조회 (DTO 조회)
    public List<OrderSimpleQueryDto> findAllDto() {
        TypedQuery<OrderSimpleQueryDto> query = entityManager.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o join o.member m join o.delivery d", OrderSimpleQueryDto.class);
        return query.getResultList();
    }

}