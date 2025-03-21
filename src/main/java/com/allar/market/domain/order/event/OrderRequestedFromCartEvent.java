package com.allar.market.domain.order.event;

import com.allar.market.domain.cart.domain.Cart;

public record OrderRequestedFromCartEvent(Cart cart) {
}
