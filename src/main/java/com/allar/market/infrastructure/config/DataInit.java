package com.allar.market.infrastructure.config;

import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.domain.CartItem;
import com.allar.market.domain.cart.repository.CartItemRepository;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.customer.repository.CustomerRepository;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.domain.OrderItem;
import com.allar.market.domain.order.repository.OrderItemRepository;
import com.allar.market.domain.order.repository.OrderRepository;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInit implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void run(String... args) throws Exception {

        Product p1 = createProduct("상품1", BigDecimal.valueOf(100), 100);
        Product p2 = createProduct("상품2", BigDecimal.valueOf(100), 100);
        Product p3 = createProduct("상품3", BigDecimal.valueOf(100), 100);

        Product product1 = productRepository.save(p1);
        Product product2 = productRepository.save(p2);
        Product product3 = productRepository.save(p3);

        List<Product> products = productRepository.findAll();
        products.forEach(p -> log.info("Test Product id:{}, name:{}",p.getId(),p.getName()));

        Customer c = Customer.builder().email("aaa@aaa.com").build();
        Customer customer  = customerRepository.save(c);
        Cart cart = customer.getCart();
        log.info("Test Cart id:{}", cart.getId());

        CartItem cartItem1 = new CartItem(product1, 10, cart);
        CartItem cartItem2 = new CartItem(product2, 5, cart);
        CartItem cartItem3 = new CartItem(product3, 3, cart);

        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);
        cartItemRepository.save(cartItem3);

        Order order = Order.builder()
                .customer(customer)
                .build();

        List<CartItem> cartItems = cartItemRepository.findAll();
        for(CartItem item : cartItems){
            log.info("Test CartItem itemId:{}, productId:{}, quantity:{}", item.getId(), item.getProduct().getId(), item.getQuantity());
        }
        OrderItem orderItem1 = new OrderItem(product1, 10);
        OrderItem orderItem2 = new OrderItem(product2, 5);
        OrderItem orderItem3 = new OrderItem(product3, 3);
        orderItem1.setOrder(order);
        orderItem2.setOrder(order);
        orderItem3.setOrder(order);

        orderRepository.save(order);
        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);

        List<OrderItem> orderItems = orderItemRepository.findAll();
        for(OrderItem item : orderItems){
            log.info("Test OrderItem itemId:{}, productId:{}, quantity:{}", item.getId(), item.getProduct().getId(), item.getQuantity());
        }
    }

    private Product createProduct(String name, BigDecimal price, int quantity) {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
