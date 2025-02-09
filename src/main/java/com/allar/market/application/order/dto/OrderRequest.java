package com.allar.market.application.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(

        @NotNull(message = "고객 ID는 필수입니다.")
        Long customerId,
        @NotNull(message = "주문 상품 리스트는 null일 수 없습니다.")
        @NotEmpty(message = "주문할 상품이 비어있습니다.")
        List<OrderItemRequest> orderItems
) {
}
