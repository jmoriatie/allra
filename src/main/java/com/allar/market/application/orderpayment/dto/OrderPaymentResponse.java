package com.allar.market.application.orderpayment.dto;

import lombok.Builder;

@Builder
public record OrderPaymentResponse(
        String status,
        String transactionId,
        String message
) {
}
