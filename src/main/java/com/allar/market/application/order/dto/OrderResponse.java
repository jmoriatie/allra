package com.allar.market.application.order.dto;

import com.allar.market.domain.order.domain.Order;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record OrderResponse(
        Long id,
        List<OrderItemResponse> orderItems,
        BigDecimal totalPrice) {

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderItems(order.getItems().stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toList()))
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
