package com.allar.market.application.order.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record OrderItemRequest(
        Long productId,
        int quantity) {
}
