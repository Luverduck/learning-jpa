package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final InitService initService;

    // 스프링 빈 생성 및 의존관계 주입 후 자동으로 실행되는 메소드
    @PostConstruct
    public void init() {
        initService.init1();
        initService.init2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager entityManager;

        public void init1() {
            Member member = createMember("userA", "서울", "1", "1111");
            entityManager.persist(member);
            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            entityManager.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            entityManager.persist(book2);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void init2() {
            Member member = createMember("userB", "진주", "2", "2222");
            entityManager.persist(member);
            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            entityManager.persist(book1);
            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            entityManager.persist(book2);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            entityManager.persist(order);
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }

}
