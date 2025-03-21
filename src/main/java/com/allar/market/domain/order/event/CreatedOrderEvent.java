package com.allar.market.domain.order.event;

import com.allar.market.domain.order.domain.Order;

public record CreatedOrderEvent(
        Long cartId,
        Order order
) {}
