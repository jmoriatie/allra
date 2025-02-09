package com.allar.market.application.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CartOrderRequest(
        @NotNull(message = "장바구니 ID는 필수입니다.")
        Long cartId
) {
}
