package com.allar.market.application.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ReceiverInfoRequest(
        @NotBlank(message = "받을 분을 작성해주세요.")
        String receiver
) {
}
