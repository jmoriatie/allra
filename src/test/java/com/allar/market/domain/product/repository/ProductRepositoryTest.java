package com.allar.market.domain.product.repository;

import com.allar.market.domain.product.domain.Product;
import com.allar.market.global.config.TestAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

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

}