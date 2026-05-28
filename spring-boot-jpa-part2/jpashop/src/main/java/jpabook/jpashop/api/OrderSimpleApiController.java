package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 주문 조회 (엔티티 직접 반환)
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders;
    }

    // 주문 조회 (DTO 반환)
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // 엔티티를 주문 조회 응답 DTO로 변환한 후 반환
        return orders.stream()
                        .map(o -> new SimpleOrderDto(o))
                        .toList();
    }

    // 주문 조회 응답 DTO
    @Data
    static class SimpleOrderDto {
        private Long id;
        private String name;
        private LocalDateTime date;
        private OrderStatus status;
        private Address address;
        public SimpleOrderDto(Order order) {
            id = order.getId();
            name = order.getMember().getName(); // LAZY 프록시 초기화
            date = order.getOrderDate();
            status = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 프록시 초기화
        }
    }

}
