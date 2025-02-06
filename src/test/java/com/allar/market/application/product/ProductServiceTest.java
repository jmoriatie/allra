package com.allar.market.application.product;

import com.allar.market.application.product.dto.ProductResponse;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("구매가능 상품 조회 - 수량이 0 초과인 상품")
    void findPossiblePurchaseProductsTest() {


        //given
        List<Product> products = List.of(
                createProduct("상품1", 10, BigDecimal.valueOf(100)),
                createProduct("상품2", 20, BigDecimal.valueOf(200)),
                createProduct("상품3", 30, BigDecimal.valueOf(300))
        );
        //when
        when(productRepository.findPossiblePurchaseProducts()).thenReturn(products);
        //then
        List<ProductResponse> result = productService.findPossiblePurchaseProducts();

        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result.get(0).name()).isEqualTo("상품1");
        Assertions.assertThat(result.get(1).quantity()).isEqualTo(20);
        Assertions.assertThat(result.get(2).price()).isEqualTo(BigDecimal.valueOf(300));
    }

    private Product createProduct(String name, int quantity, BigDecimal price) {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }
}