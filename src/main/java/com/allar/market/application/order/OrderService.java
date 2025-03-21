package com.allar.market.application.order;

import com.allar.market.application.order.dto.CartOrderRequest;
import com.allar.market.application.order.dto.OrderRequest;
import com.allar.market.application.order.dto.OrderResponse;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.repository.CartRepository;
import com.allar.market.domain.common.cache.OrderResultCache;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.customer.repository.CustomerRepository;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.domain.OrderState;
import com.allar.market.domain.order.event.OrderRequestedFromCartEvent;
import com.allar.market.domain.order.repository.OrderRepository;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ApplicationEventPublisher publisher;
    private final OrderResultCache orderResultCache;

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
     * TODO pub/sub
     *  OrderResultCache -> 아키택처 변경? commandGateway 사용 등 변경
     *  event -> kafka로 변경
     * @param cartOrderRequest
     */
    public CompletableFuture<OrderResponse> createOrderFromCart(CartOrderRequest cartOrderRequest) {
        Cart cart = cartRepository.findById(cartOrderRequest.cartId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 ID 입니다."));

        // TODO future cache에 넣어서 실제 수신
        //  -> future Processing 으로 DB 저장 (Redis?)
        //  -> Listener 쪽에서 success or failure update 하기
        CompletableFuture<OrderResponse> resultFuture = new CompletableFuture<>();
        orderResultCache.put(cart.getId(), resultFuture);

        OrderRequestedFromCartEvent event = new OrderRequestedFromCartEvent(cart);
        publisher.publishEvent(event);

        // TODO 직접수신 -> DB 불러오기 변경
        return resultFuture
                .whenComplete((result, ex) -> printLog(ex, cart.getId()))
                .exceptionally(ex -> returnEmptyResponse(ex, cart.getId()));
    }

    private OrderResponse returnEmptyResponse(Throwable ex, Long cartId) {
        log.error("주문 생성 실패: cartId={} ", cartId, ex);
        return OrderResponse.builder()
                .id(null)
                .orderItems(Collections.emptyList())
                .totalPrice(BigDecimal.ZERO)
                .build();
    }

    private void printLog( Throwable ex, Long cartId) {
        if(ex != null){
             log.error("주문 생성 실패: cartId={} ", cartId, ex);
        }else {
            log.info("주문 생성 성공: cartId={} ", cartId);
        }
    }

    /**
     * 주문 조회 - 주문완료 제외
     * @param orderId
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderWithoutComplete(Long orderId){
        Order order = orderRepository.findByIdAndOrderStateNot(orderId, OrderState.COMPLETE)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문 ID 입니다."));
        return OrderResponse.from(order);
    }

    /**
     * 주문 완료
     * @param orderId
     */
    public void completePayment(Long orderId){
        Order order = orderRepository.findByIdAndOrderStateNot(orderId, OrderState.COMPLETE)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문 ID 입니다."));
        order.completePayment();
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