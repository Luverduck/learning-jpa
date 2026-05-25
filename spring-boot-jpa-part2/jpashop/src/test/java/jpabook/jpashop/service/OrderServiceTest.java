package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    // 주문
    @Test
    public void order() {
        // 회원 생성
        Member member = createMember();
        // 상품 생성
        Item item = createBook("시골 JPA", 10000, 10);
        // 주문 수량
        int orderCount = 2;
        // 상품 주문
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // 주문 조회
        Order getOrder = orderRepository.findOne(orderId);
        // 주문 상태 검증
        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        // 주문 상품 종류의 수 검증
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류의 수가 정확해야 한다.");
        // 주문 전체 가격 검증
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "상품 주문 시 상태는 ORDER");
        // 상폼 재고 검증
        Assertions.assertEquals(8, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    // 주문 (재고 부족 시 예외 발생)
    @Test
    public void orderNotEnoughStock() {
        // 회원 생성
        Member member = createMember();
        // 상품 생성
        Item item = createBook("시골 JPA", 10000, 10);
        // 주문 수량
        int orderCount = 11;
        // 상품 주문 > 재고 수량 이상 주문 시 예외 발생
        Assertions.assertThrows(
            NotEnoughStockException.class,
            () -> orderService.order(member.getId(), item.getId(), orderCount),
            "재고 수량 부족 예외가 발생해야 한다."
        );
    }

    // 주문 취소
    @Test
    public void cancelOrder() {
        // 회원 생성
        Member member = createMember();
        // 상품 생성
        Item item = createBook("시골 JPA", 10000, 10);
        // 주문 수량
        int orderCount = 2;
        // 상품 주문
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // 주문 취소
        orderService.cancelOrder(orderId);
        // 주문 조회
        Order getOrder = orderRepository.findOne(orderId);
        // 주문 상태 검증
        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 상태는 CANCEL 이다.");
        // 상폼 재고 검증
        Assertions.assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    // 테스트용 회원 생성
    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        entityManager.persist(member);
        return member;
    }

    // 테스트용 상품 생성
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);
        return book;
    }

}