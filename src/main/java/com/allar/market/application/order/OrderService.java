package com.allar.market.application.order;

import com.allar.market.application.order.dto.CartOrderRequest;
import com.allar.market.application.order.dto.OrderRequest;
import com.allar.market.application.order.dto.OrderResponse;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.repository.CartRepository;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.customer.repository.CustomerRepository;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.domain.OrderState;
import com.allar.market.domain.order.repository.OrderRepository;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    /**
     * 일반 주문
     * @param orderRequest
     */
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.customerId())
                .orElseThrow(() -> new IllegalArgumentException("없는 고객 ID 입니다."));

        Order order = Order.builder()
                .customer(customer)
                .build();

        orderRequest.orderItems().forEach(item -> {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("없는 상품 ID 입니다."));
            order.addItem(product, item.quantity());
        });

        return OrderResponse.from(orderRepository.save(order));
    }

    /**
     * 장바구니 주문
     * @param cartOrderRequest
     */
    public OrderResponse createOrderFromCart(CartOrderRequest cartOrderRequest) {
        Cart cart = cartRepository.findById(cartOrderRequest.cartId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 ID 입니다."));
        Order order = Order.builder()
                .customer(cart.getCustomer())
                .build();
        order.addFromCart(cart);

        return OrderResponse.from(orderRepository.save(order));
    }

    /**
     * 주문 조회 - 주문완료 제외
     * @param orderId
     */
    public OrderResponse getOrderWithoutComplete(Long orderId){
        Order order = orderRepository.findByIdAndOrderStateNot(orderId, OrderState.COMPLETE)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문 ID 입니다."));
        return OrderResponse.from(order);
    }

    /**
     * 주문 취소
     * @param orderId
     */
    public void cancel(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문 ID 입니다."));
        order.cancel();
    }
}
