package com.allar.market.application.product.dto;

import com.allar.market.domain.product.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest(
        @NotBlank(message = "상품명은 필수입니다.")
        String name,
        String description,
        @NotNull(message = "상품가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0 미만일 수 없습니다.")
        BigDecimal price,
        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 0, message = "수량은 0 미만일 수 없습니다.")
        int quantity
) {}
