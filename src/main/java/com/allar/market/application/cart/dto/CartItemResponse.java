package com.allar.market.application.cart.dto;

import com.allar.market.domain.cart.domain.CartItem;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(
        Long productId,
        String productName,
        BigDecimal productPrice,
        int quantity,
        BigDecimal totalPrice
) {
    public static CartItemResponse from(CartItem cartItem) {
        return CartItemResponse.builder()
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productPrice(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }
}
