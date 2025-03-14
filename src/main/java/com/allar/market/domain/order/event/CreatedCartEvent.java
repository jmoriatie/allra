package com.allar.market.domain.order.event;

import com.allar.market.application.order.dto.OrderResponse;
import com.allar.market.domain.cart.domain.Cart;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class CreatedCartEvent {
    private final Cart cart;
//    private final String orderId;
    private final CompletableFuture<OrderResponse> resultFuture;
}
