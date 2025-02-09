package com.allar.market.application.product;

import com.allar.market.application.product.dto.ProductResponse;
import com.allar.market.domain.order.domain.OrderItem;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
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

        assertThat(result).hasSize(3);
        assertThat(result.get(0).name()).isEqualTo("상품1");
        assertThat(result.get(1).quantity()).isEqualTo(20);
        assertThat(result.get(2).price()).isEqualTo(BigDecimal.valueOf(300));
    }

    @Test
    @DisplayName("상품 수량 감소")
    void decreaseOrderedQuantity(){
        Product product1 = createProduct(991L, "상품1", BigDecimal.valueOf(100), 10);
        Product product2 = createProduct(992L, "상품2", BigDecimal.valueOf(100), 10);
        Product product3 = createProduct(993L, "상품3", BigDecimal.valueOf(100), 10);

        List<OrderItem> orderItems = List.of(
                new OrderItem(product1, 10),
                new OrderItem(product2, 5),
                new OrderItem(product3, 3)
        );
        //when

        Set<Long> productIds = Set.of(product1.getId(), product2.getId(), product3.getId());
        List<Product> products = List.of(product1, product2, product3);
        when(productRepository.findAllById(productIds))
                .thenReturn(products);
        // then
        productService.decreaseOrderedQuantity(orderItems);

        //then
        assertThat(product1.getQuantity()).isEqualTo(0);
        assertThat(product2.getQuantity()).isEqualTo(5);
        assertThat(product3.getQuantity()).isEqualTo(7);
    }

    private Product createProduct(String name, int quantity, BigDecimal price) {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }

    private Product createProduct(Long id, String name, BigDecimal price, int quantity) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }
}