package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {

    private Long id;
    private String name;
    private LocalDateTime date;
    private OrderStatus status;
    private Address address;

    private String itemName;
    private int price;
    private int count;

    public OrderFlatDto(Long id, String name, LocalDateTime date, OrderStatus status, Address address, String itemName, int price, int count) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.address = address;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
    }

}