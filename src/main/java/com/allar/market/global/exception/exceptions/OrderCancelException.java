package com.allar.market.global.exception.exceptions;

public class OrderCancelException extends RuntimeException{
    public OrderCancelException() {
        super("주문을 취소할 수 없습니다.");
    }

    public OrderCancelException(String message) {
        super(message);
    }
}
