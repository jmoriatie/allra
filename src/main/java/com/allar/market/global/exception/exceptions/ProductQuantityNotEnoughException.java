package com.allar.market.global.exception.exceptions;

public class ProductQuantityNotEnoughException extends IllegalArgumentException{
    public ProductQuantityNotEnoughException() {
        super("상품 수량이 부족합니다");
    }

    public ProductQuantityNotEnoughException(String message) {
        super(message);
    }
}
