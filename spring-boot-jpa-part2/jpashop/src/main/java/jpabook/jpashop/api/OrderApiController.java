package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // 주문 조회 (엔티티 직접 반환)
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // 지연 로딩 프록시 초기화
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
        }
        return orders;
    }

    // 주문 조회 (엔티티를 DTO로 변환)
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> list = orders.stream().map(o -> new OrderDto(o)).toList();
        return list;
    }

    // 주문 조회 (엔티티를 DTO로 변환 - 페치 조인 최적화)
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> list = orders.stream().map(o -> new OrderDto(o)).toList();
        return list;
    }

    // 주문 조회 (엔티티를 DTO로 변환 - 페이징과 한계 돌파)
    @GetMapping("/api/v4/orders")
    public List<OrderDto> ordersV4(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
        ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> list = orders.stream().map(o -> new OrderDto(o)).toList();
        return list;
    }

    // 주문 조회 (DTO 직접 조회)
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllDto();
    }

    // 주문 조회 (DTO 직접 조회 - )
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        return orderQueryRepository.findAllDtoOptimization();
    }

    // 주문 조회 응답 DTO
    @Getter
    static class OrderDto {
        private Long id;
        private String name;
        private LocalDateTime date;
        private OrderStatus status;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            id = order.getId();
            name = order.getMember().getName();
            date = order.getOrderDate();
            status = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).toList();
        }
    }

    // 주문상품 응답 DTO
    @Getter
    static class OrderItemDto {
        private String name;
        private int price;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            name = orderItem.getItem().getName();
            price = orderItem.getItem().getPrice();
            count = orderItem.getCount();
        }
    }

}