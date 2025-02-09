package com.allar.market.global.exception.exceptions;

public class PaymentClosedException extends RuntimeException{
    public PaymentClosedException() {
        super("이미 종료된 결제건 입니다.");
    }

    public PaymentClosedException(String message) {
        super(message);
    }
}
