package com.allar.market.domain.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("상품(Product) 도메인 테스트")
class ProductTest {

    @Test
    @DisplayName("Exception 상품 생성 가격이 음수일 경우")
    void createProductNegativePrice(){
        Product.ProductBuilder productBuilder = Product.builder()
                .name("테스트상품1")
                .quantity(5)
                .price(BigDecimal.valueOf(-10000));

        assertThrows(IllegalArgumentException.class, productBuilder::build);
    }

    @Test
    @DisplayName("상품 수량 증가")
    public void increaseQuantity(){
        // given
        Product product = Product.builder()
                .name("테스트상품1")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();
        //when
        product.increaseQuantity(10);
        //then
        assertEquals(20, product.getQuantity());
    }

    @Test
    @DisplayName("상품 수량 감소")
    public void decreaseQuantity(){
        // given
        Product product = Product.builder()
                .name("테스트상품1")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();
        //when
        product.decreaseQuantity(10);
        //then
        assertEquals(0, product.getQuantity());
    }

    @Test
    @DisplayName("Exception - 상품 수량 초과 감소")
    public void decreaseQuantityOverQuantity(){
        // given, when
        Product product = Product.builder()
                .name("테스트상품1")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();
        //then
        assertThrows(IllegalArgumentException.class, () -> product.decreaseQuantity(11));
    }

    @Test
    @DisplayName("수량 음수로 내려갈 경우 false 반환")
    void hasNotEnoughQuantity(){
        // given, when
        Product product = Product.builder()
                .name("테스트상품1")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();
        //then
        assertFalse(product.hasEnoughQuantity(500));
    }

    @Test
    @DisplayName("정상처리 - 수량 음수로 내려갈 경우")
    void hasEnoughQuantity(){
        // given, when
        Product product = Product.builder()
                .name("테스트상품1")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();
        //then
        assertTrue(product.hasEnoughQuantity(5));
    }
}