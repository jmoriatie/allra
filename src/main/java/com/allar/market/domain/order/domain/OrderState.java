package com.allar.market.domain.order.domain;

public enum OrderState {
    WAITING("대기중"),
    PAID("결제완료"),
    PREPARING("상품준비중"),
    DELIVERING("배송중"),
    COMPLETE("배송완료"),
    CANCELLED("취소됨");

    private final String state;

    OrderState(String state) {
        this.state = state;
    }

    public boolean isCancellable(){
        return this == WAITING || this == PAID || this == PREPARING;
    }
}
