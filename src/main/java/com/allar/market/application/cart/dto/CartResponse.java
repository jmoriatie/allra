package com.allar.market.application.cart.dto;

import com.allar.market.domain.cart.domain.Cart;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartResponse(
        Long id,
        List<CartItemResponse> cartItems,
        BigDecimal totalPrice
) {
    public static CartResponse from(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .cartItems(cart.getItems().stream()
                        .map(CartItemResponse::from)
                        .toList())
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
