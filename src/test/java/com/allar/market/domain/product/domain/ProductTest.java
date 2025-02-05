package com.allar.market.domain.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


//@DataJpaTest
//@Import(TestAuditingConfig.class)
@DisplayName("상품(Product) 도메인 테스트")
class ProductTest {


    @Test
    @DisplayName("상품 생성 음수시 오류")
    void createProductNegativePrice(){
        //give
        Product.builder()
                .name("테스트상품1")
                .quantity(5)
                .price(BigDecimal.valueOf(-10000))
                .build();


        //when

        //then
    }

    @Test
    @DisplayName("상품 수량 증가")
    public void increaseQuantity(){
    }

    @Test
    @DisplayName("상품 수량 감소")
    public void decreaseQuantity(){
    }

    @Test
    @DisplayName("에러발생 - 수량 음수로 내려갈 경우")
    void hasNotEnoughQuantity(){
    }

    @Test
    @DisplayName("정상처리 - 수량 음수로 내려갈 경우")
    void hasEnoughQuantity(){
        int quantity = 0;
    }
}