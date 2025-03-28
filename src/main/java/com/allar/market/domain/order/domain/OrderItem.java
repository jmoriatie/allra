package com.allar.market.domain.order.domain;

import com.allar.market.domain.cart.domain.CartItem;
import com.allar.market.domain.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter // Order class와 생성주기 다름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 단방향 참조

    private BigDecimal price;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    private OrderItem(CartItem cartItem) {
        this.product = cartItem.getProduct();
        this.price = product.getPrice();
        this.quantity = cartItem.getQuantity();
    }

    /**
     * 장바구니 아이템으로부터 생성
     * @param cartItem
     * @return
     */
    public static OrderItem addFromCartItem(CartItem cartItem){
        return new OrderItem(cartItem);
    }

    public BigDecimal getTotalPrice(){
        return this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }

}
