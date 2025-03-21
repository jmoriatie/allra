package com.allar.market.domain.order.event;

public record CreateOrderFailedEvent(
        Long cartId,
        Exception exception) {
}
