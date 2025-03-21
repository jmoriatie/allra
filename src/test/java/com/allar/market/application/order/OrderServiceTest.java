package com.allar.market.application.order;

import com.allar.market.application.order.dto.CartOrderRequest;
import com.allar.market.application.order.dto.OrderResponse;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.repository.CartRepository;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.customer.repository.CustomerRepository;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.repository.OrderRepository;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("통합테스트: Order 생성 시 cart 생성 테스트, pub/sub 테스트")
    void createOrderFromCart() {
        // given
        Customer savedCustomer = createTestCustomer();
        Cart savedCart = cartRepository.save(savedCustomer.getCart());

        // request 생성
        CartOrderRequest request = new CartOrderRequest(savedCart.getId());

        // when
        CompletableFuture<OrderResponse> futureResult = orderService.createOrderFromCart(request);

        // then
        await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // 주문이 DB에 저장되었는지 검증
                    List<Order> orders = orderRepository.findOrderWithCustomerByCustomerId(savedCustomer.getId());
                    assertFalse(orders.isEmpty());

                    // future가 완료되었는지 검증
                    assertTrue(futureResult.isDone());
                });

        try {
            OrderResponse result = futureResult.get();
            System.out.println("result=" + result);
            assertNotNull(result);
            assertNotNull(result.id());
        } catch (Exception e) {
            fail("future 미완료:" + e.getMessage());
        }
    }

    @Test
    @DisplayName("통합테스트: Order 생성 시 cart와 product 관련 생성 테스트 - pub/sub 테스트")
    void setProductTest() {
        // given
        Customer savedCustomer = createTestCustomer();
        Cart savedCart = createTestCartAndProductSet(savedCustomer);

        // request 생성
        CartOrderRequest request = new CartOrderRequest(savedCart.getId());

        // when
        CompletableFuture<OrderResponse> futureResult = orderService.createOrderFromCart(request);

        // then - pub/sub order 저장
        await().atMost(15, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // 주문이 DB에 저장되었는지 검증
                    List<Order> orders = transactionTemplate.execute(status ->
                            orderRepository.findOrderWithCustomerByCustomerId(savedCustomer.getId()));
                    assertFalse(orders.isEmpty());

                    // future가 완료되었는지 검증
                    assertTrue(futureResult.isDone());
                });

        try {
            OrderResponse result = futureResult.get();
            System.out.println("### RESULT###\n" + result);
            assertNotNull(result);
            assertNotNull(result.id());
            assertEquals(3, result.orderItems().size());
        } catch (Exception e) {
            fail("future 미완료:" + e.getMessage());
        }
    }

    private Cart createTestCartAndProductSet(Customer savedCustomer) {
        return transactionTemplate.execute(status -> {
            Cart savedCart = cartRepository.save(savedCustomer.getCart());

            // 상품 생성 및 Cart에 세팅
            List<Product> products = List.of(
                    Product.builder().name("테스트 상품1")
                            .price(BigDecimal.valueOf(1000))
                            .quantity(10)
                            .build(),
                    Product.builder().name("테스트 상품2")
                            .price(BigDecimal.valueOf(2000))
                            .quantity(20)
                            .build(),
                    Product.builder().name("테스트 상품3")
                            .price(BigDecimal.valueOf(3000))
                            .quantity(30)
                            .build()
            );
            List<Product> savedProducts = productRepository.saveAll(products);

            savedProducts.forEach(p -> savedCart.addItem(p, 5)); // cart 에 product 세팅
            return savedCart;
        });
    }

    private Customer createTestCustomer() {
        return transactionTemplate.execute(status -> {
            Customer customer = Customer.builder().email("aaa@aaa.com").password("aa").build();
            return customerRepository.save(customer);
        });
    }
}