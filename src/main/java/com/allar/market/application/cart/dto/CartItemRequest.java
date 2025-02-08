package com.allar.market.application.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CartItemRequest(

        @NotBlank(message = "상품 ID는 필수입니다.")
        Long productId,
        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "최소 수량은 1개 입니다.")
        int quantity) {}
