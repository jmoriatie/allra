package com.allar.market.application.order.dto;

import com.allar.market.domain.order.domain.OrderItem;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponse(
        Long productId,
        String productName,
        BigDecimal price,
        int quantity,
        BigDecimal totalPrice
) {
    public static OrderItemResponse from(OrderItem orderItem){
        return OrderItemResponse.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}
