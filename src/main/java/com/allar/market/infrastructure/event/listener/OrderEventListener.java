package com.allar.market.infrastructure.event.listener;

import com.allar.market.application.order.dto.OrderResponse;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.common.cache.OrderResultCache;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.customer.repository.CustomerRepository;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.event.CreateOrderFailedEvent;
import com.allar.market.domain.order.event.CreatedOrderEvent;
import com.allar.market.domain.order.event.OrderRequestedFromCartEvent;
import com.allar.market.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;
    private final OrderResultCache orderResultCache;
    private final TransactionTemplate transactionTemplate;


    /**
     * 카트(Cart)에서 주문하기 눌렀을 때, Order 객체 생성
     * @param event
     */
    @Async
    @EventListener
    public void handleOrderRequestedFromCartEvent(OrderRequestedFromCartEvent event){
        Cart cart = event.cart();
        try {
            Customer customer = customerRepository.findById(cart.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("없는 customer 입니다. customerId=" + cart.getCustomer().getId()));

            Order order = Order.builder()
                    .customer(customer)
                    .build();
            order.addFromCart(cart);

            // TX 최소화
            Order savedOrder = transactionTemplate.execute(status -> orderRepository.save(order));

            publisher.publishEvent(new CreatedOrderEvent(cart.getId(), savedOrder));
        } catch (Exception e) {
            publisher.publishEvent(new CreateOrderFailedEvent(cart.getId(), e));
        }
    }


    /**
     * Order 생성 이벤트 성공
     *  future complete 처리, 캐시 삭제
     * @param event
     */
    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCreatedOrderEvent(CreatedOrderEvent event){
        CompletableFuture<OrderResponse> future = orderResultCache.get(event.cartId());
        if(future != null){
            OrderResponse orderResponse = OrderResponse.from(event.order());
            future.complete(orderResponse);
            orderResultCache.remove(event.cartId());
        }
    }

    /**
     * Order 생성 이벤트 실패
     *  future excetption 처리, 캐시 삭제
    */
    @Async
    @EventListener
    public void handleCreatedCartEvent(CreateOrderFailedEvent event){
        CompletableFuture<OrderResponse> future = orderResultCache.get(event.cartId());
        if(future != null){
            future.completeExceptionally(event.exception());
            orderResultCache.remove(event.cartId());
        }
    }
}
