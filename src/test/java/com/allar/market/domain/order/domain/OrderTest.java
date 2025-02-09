package com.allar.market.domain.order.domain;

import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.global.exception.exceptions.OrderCancelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order 도메인 테스트")
class OrderTest {

    private Customer customer;

    @BeforeEach
    void beforeEach() {
        customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
    }

    @Test
    @DisplayName("OrderItem 생성")
    void addItem() {
        // given
        Product product = Product.builder().name("테스트 상품")
                .price(BigDecimal.valueOf(1000))
                .description("테스트 상품 입니다.")
                .quantity(10)
                .build();
        // when
        Order order = Order.builder()
                .customer(customer)
                .build();
        order.addItem(product, 5);

        // then
        assertEquals(1, order.getItems().size());
        assertEquals(BigDecimal.valueOf(5000), order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 상태변경 테스트")
    void updateOrderState() {
        // given
        Product product = Product.builder().name("테스트 상품")
                .price(BigDecimal.valueOf(1000))
                .description("테스트 상품 입니다.")
                .quantity(10)
                .build();
        // when
        Order order = Order.builder()
                .customer(customer)
                .build();

        order.updateOrderState(OrderState.COMPLETE);

        // then
        assertEquals(OrderState.COMPLETE, order.getOrderState());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void cancel() {
        // given
        Product product = Product.builder().name("테스트 상품")
                .price(BigDecimal.valueOf(1000))
                .description("테스트 상품 입니다.")
                .quantity(10)
                .build();
        // when
        Order order = Order.builder()
                .customer(customer)
                .build();
        order.updateOrderState(OrderState.PREPARING);
        order.cancel();

        // then
        assertEquals(OrderState.CANCELLED, order.getOrderState());
    }

    @Test
    @DisplayName("실패 - 주문 취소 오류")
    void cancelException() {
        Order order = Order.builder()
                .customer(customer)
                .build();
        order.updateOrderState(OrderState.DELIVERING); // 배송중 변경 불가

        // then
        assertThrows(OrderCancelException.class,
                () -> order.cancel());
    }
}