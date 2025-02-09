package com.allar.market.domain.payment.domain;

public enum PaymentState {
    WAITING("결제대기"),
    COMPLETED("결제완료"),
    FAILED("실패함"),
    CANCELLED("취소됨"),
    REFUNDED("환불됨");

    private final String state;

    PaymentState(String state) {
        this.state = state;
    }

    public boolean isPossible(){
        return this == WAITING || this == FAILED;
    }

    public boolean isClosed(){
        return this == COMPLETED || this == CANCELLED || this == REFUNDED;
    }
}
