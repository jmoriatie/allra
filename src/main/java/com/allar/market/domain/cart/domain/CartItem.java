package com.allar.market.domain.cart.domain;

import com.allar.market.domain.common.BaseEntity;
import com.allar.market.domain.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 단방향 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItem(Product product, int quantity, Cart cart) {
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;
    }

    public void addQuantity(int quantity){
        this.quantity += quantity;
    }

    public void updateQuantity(int quantity){
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice(){
        return this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }
}
