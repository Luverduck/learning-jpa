package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
public class OrderQueryDto {

    private Long id;
    private String name;
    private LocalDateTime date;
    private OrderStatus status;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long id, String name, LocalDateTime date, OrderStatus status, Address address) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.address = address;
    }

    public OrderQueryDto(Long id, String name, LocalDateTime date, OrderStatus status, Address address, List<OrderItemQueryDto> orderItems) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.address = address;
        this.orderItems = orderItems;
    }

}