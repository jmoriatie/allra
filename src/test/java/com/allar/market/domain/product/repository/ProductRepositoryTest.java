package com.allar.market.domain.product.repository;

import com.allar.market.domain.product.domain.Product;
import com.allar.market.infrastructure.config.TestAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestAuditingConfig.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("BaseEntity 생성 Test")
    void  createBaseEntity(){
        Product entity = Product.builder()
                .name("테스트상품1")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();

        Product product = productRepository.save(entity);

        assertEquals("test-user", product.getCreatedBy());
        assertEquals("test-user", product.getUpdatedBy());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    @DisplayName("구매가능 상품 조회 - 수량이 0 초과인 상품")
    void possiblePurchaseProducts(){
        //given
        Product entity1 = Product.builder()
                .name("테스트상품1")
                .description("첫 번째 상품입니다.")
                .quantity(10)
                .price(BigDecimal.valueOf(10000))
                .build();

        Product entity2 = Product.builder()
                .name("테스트상품2")
                .description("수량이 0인 상품입니다.")
                .quantity(0)
                .price(BigDecimal.valueOf(10000))
                .build();

        Product entity3 = Product.builder()
                .name("테스트상품3")
                .description("세 번째 상품입니다.")
                .quantity(20)
                .price(BigDecimal.valueOf(10000))
                .build();
        //when
        productRepository.saveAll(List.of(entity1, entity2, entity3));
        List<Product> possiblePurchaseProducts = productRepository.findPossiblePurchaseProducts();
        //then
        assertEquals(2, possiblePurchaseProducts.size());
        assertFalse(possiblePurchaseProducts.contains(entity2));
        assertTrue(possiblePurchaseProducts.contains(entity1));
        assertTrue(possiblePurchaseProducts.contains(entity3));
    }
}