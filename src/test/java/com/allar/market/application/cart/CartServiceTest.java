package com.allar.market.application.cart;

import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.domain.CartItem;
import com.allar.market.domain.cart.repository.CartRepository;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.order.domain.OrderItem;
import com.allar.market.domain.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Test
    @DisplayName("주문된 상품 장바구니에서 제거")
    void removeOrderedCartItem() {
        // given
        Product product1 = createProduct(1L, "상품1", BigDecimal.valueOf(100), 10);
        Product product2 = createProduct(2L, "상품2", BigDecimal.valueOf(100), 10);
        Product product3 = createProduct(3L, "상품3", BigDecimal.valueOf(100), 10);

        Long cartId = 999L;
        Cart cart = createCart(cartId);

        cart.addItem(product1, 10);
        cart.addItem(product2, 10);
        cart.addItem(product3, 10);

        List<OrderItem> orderItems = createOrderItems(product1, product3);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // when
        cartService.removeOrderedCartItem(999L, orderItems);

        // then
        assertEquals(1, cart.getItems().size());
        CartItem existCartItem = cart.getItems().stream().findFirst().get();
        assertEquals("상품2",existCartItem.getProduct().getName());
    }

    private static List<OrderItem> createOrderItems(Product product1, Product product3) {
        List<OrderItem> orderItems = List.of(
                new OrderItem(product1, 10),
                new OrderItem(product3, 10)
        );
        return orderItems;
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

    private Cart createCart(Long id) {
        Customer customer = Customer.builder().email("aaa@aaa.com").build();
        Cart cart = Cart.builder()
                .customer(customer)
                .build();
        ReflectionTestUtils.setField(cart, "id", id);
        return cart;
    }
}