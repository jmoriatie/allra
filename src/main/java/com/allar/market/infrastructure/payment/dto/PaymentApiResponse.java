package com.allar.market.infrastructure.payment.dto;

import lombok.Builder;

@Builder
public record PaymentApiResponse(
        String status,
        String transactionId,
        String message
) {
    public boolean isSuccess(){
        return "SUCCESS".equals(status);
    }

    public boolean isFailed(){
        return "FAILED".equals(status);
    }
}
