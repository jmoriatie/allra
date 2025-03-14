package com.allar.market.domain.cart.domain;

import com.allar.market.domain.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    @DisplayName("카트아이템 전체 가격 불러오기 테스트")
    void getTotalPrice() {
        // given
        Product product = Product.builder().name("테스트 상품1")
                .price(BigDecimal.valueOf(1000))
                .quantity(10) // 100개 수량 product
                .build();
        // when
        CartItem cartItem = new CartItem(product, 5, new Cart()); // 5개 담음 1000*5 = 5,000원
        // then
        assertEquals(BigDecimal.valueOf(5000), cartItem.getTotalPrice());
    }
}