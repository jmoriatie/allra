package com.allar.market.global.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

