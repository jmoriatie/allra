package com.allar.market.domain.cart.domain;

import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.global.exception.exceptions.ProductQuantityNotEnoughException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart 도메인 테스트")
class CartTest {

    private Customer customer;
    private Cart cart;

    @BeforeEach
    void beforeEach() {
        customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
        cart = Cart.builder().customer(customer).build();
    }

    @Test
    @DisplayName("상품 생성 및 카트에 담기 테스트")
    void newItem() {
        Product product = Product.builder().name("테스트 상품")
                .price(BigDecimal.valueOf(10000))
                .description("테스트 상품 입니다.")
                .quantity(10)
                .build();

        cart.addItem(product, 5); // 5개 카트에 담기

        assertEquals(BigDecimal.valueOf(50000), cart.getTotalPrice());
        assertEquals(1, cart.getItems().size());
    }

    @Test
    @DisplayName("오류 - 카트 상품 생성 수량 초과")
    void newItemException() {
        Product product = Product.builder().name("테스트 상품")
                .price(BigDecimal.valueOf(10000))
                .description("테스트 상품 입니다.")
                .quantity(10)
                .build();

        // 11개 담기 - 초과
        assertThrows(ProductQuantityNotEnoughException.class,
                () -> cart.addItem(product, 11), "상품 수량이 부족합니다.");
    }
}