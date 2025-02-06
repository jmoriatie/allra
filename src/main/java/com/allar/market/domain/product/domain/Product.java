package com.allar.market.domain.product.domain;


import com.allar.market.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;

    @Builder
    public Product(String name, String description, int quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        validatePrice(price);
        this.price = price;
    }

    public void increaseQuantity(int quantity){
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity){
        validateQuantity(quantity);
        this.quantity -= quantity;
    }

    public boolean hasEnoughQuantity(int quantity){
        return this.quantity - quantity >= 0;
    }

    private void validateQuantity(int quantity){
        if(this.quantity < quantity) throw new IllegalArgumentException("상품 수량이 부족합니다.");
    }

    private void validatePrice(BigDecimal price){
        if(price == null)
            throw new IllegalArgumentException("상품 금액을 입력해주세요.");

        if(price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("상품 금액이 0보다 작을 수 없습니다.");
    }
}
