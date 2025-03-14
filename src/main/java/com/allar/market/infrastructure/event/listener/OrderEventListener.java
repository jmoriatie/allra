package com.allar.market.infrastructure.event.listener;

import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.event.CreatedCartEvent;
import com.allar.market.domain.order.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    public void handleCreatedCartEvent(CreatedCartEvent event){
        Cart cart = event.getCart();

        Order order = Order.builder()
                .customer(cart.getCustomer())
                .build();
        order.addFromCart(cart);

        orderRepository.save(order);
    }
}
