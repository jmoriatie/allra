package com.allar.market.application.orderpayment.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderPaymentRequest(
        Long orderId,
        BigDecimal price,
        String paymentMethod
) {
}
