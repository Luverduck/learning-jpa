package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

    private Long id;
    private String name;
    private LocalDateTime date;
    private OrderStatus status;
    private Address address;

    public OrderSimpleQueryDto(Long id, String name, LocalDateTime date, OrderStatus status, Address address) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.address = address;
    }

}