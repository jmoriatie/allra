package com.allar.market.domain.order;

public enum OrderState {
    READY("준비중"),
    PAID("결제완료"),

    SUCCESS("완료");

    final String state;

    OrderState(String state) {
        this.state = state;
    }
}
