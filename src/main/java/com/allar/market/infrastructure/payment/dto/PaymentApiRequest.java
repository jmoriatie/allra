package com.allar.market.infrastructure.payment.dto;

import com.allar.market.domain.payment.domain.Payment;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentApiRequest(
        Long orderId,
        BigDecimal price
) {
    public static PaymentApiRequest from(Payment payment){
        return PaymentApiRequest.builder()
                .orderId(payment.getOrder().getId())
                .price(payment.getPrice())
                .build();
    }
}
