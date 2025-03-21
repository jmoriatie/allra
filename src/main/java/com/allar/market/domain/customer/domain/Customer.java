package com.allar.market.domain.customer.domain;

import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.common.BaseEntity;
import com.allar.market.domain.order.domain.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "customer") // 주문목록 이력관리 -> cascade, orphanRemoval 설정X
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
        // 고객 생성시 order는 없음
        this.cart = new Cart(this);
    }

    public void addOrder(Order order){
        validateOrder(order);
        this.orders.add(order);
    }

    public void validateOrder(Order order){
        if(order == null || this.orders.contains(order)){
            throw new IllegalArgumentException("Customer에 이미 존재하는 주문(Order)입니다.");
        }
    }

    public List<Order> getOrders(){
        return List.copyOf(this.orders);
    }
}
