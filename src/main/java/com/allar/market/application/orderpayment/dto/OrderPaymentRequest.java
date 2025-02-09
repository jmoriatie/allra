package com.allar.market.application.orderpayment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderPaymentRequest(
        @NotNull(message = "주문 ID는 필수 입니다.")
        Long orderId,
        @NotNull(message = "가격은 필수 입니다.")
        BigDecimal price,
        @NotBlank(message = "결제 수단은 필수 입니다.")
        String paymentMethod
) {
}
