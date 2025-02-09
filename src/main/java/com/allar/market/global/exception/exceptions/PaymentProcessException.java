package com.allar.market.global.exception.exceptions;

public class PaymentProcessException extends RuntimeException{
    public PaymentProcessException() {
        super("결제 중 오류가 발생했습니다.");
    }

    public PaymentProcessException(String message) {
        super(message);
    }
}
