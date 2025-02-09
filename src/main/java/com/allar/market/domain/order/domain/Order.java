package com.allar.market.domain.order.domain;

import com.allar.market.domain.common.BaseEntity;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>(); // product의 상태캡쳐 용도 - 변경로직 최소화

    // TODO payment 작성 필요

    @Enumerated(EnumType.STRING)
    private OrderState orderState;
    private BigDecimal totalPrice;

    // TODO 생성자 - payment 작성 후 필요에 따라 수정
    public Order(Customer customer) {
        this.customer = customer;
        this.totalPrice = BigDecimal.ZERO;
        this.orderState = OrderState.WAITING;
    }

    // TODO Order가 만들어지는 시점은 ??? -> 주문하기를 눌렀을 때
    //  ㄴ 도메인 기준에서 도메인 상태를 변경하는 애들만 생각하자 - 결합도 낮춰야함
    //  ㄴ 그냥 주문 또는 cart에 있는 item 들: 주문->결제
    //  ㄴ 생성주기 동일: 고객, 주문아이템
    //  ㄴ 생성주기 별도: Item, 결제
    // TODO Order 생성 -> 주문하기 창에서 요청하는 기능들
    // TODO Order 생성 -> 주문하기 창에서 요청하는 기능들
    // TODO 배송대기중 까지 만들면됨
    // TODO 주문 별도저장

    /**
     * 상품 주문
     * @param product
     * @param quantity
     */
    public void addItem(Product product, int quantity) {
        validateProductQuantity(product, quantity); // 재고확인
        this.items.add(new OrderItem(product, quantity));
        calculateTotalPrice();
    }

    /**
     * 주문 상태 업데이트
     * @param orderState
     */
    public void updateOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    // 상품 재고 조회
    private void validateProductQuantity(Product product, int quantity) {
        if (!product.hasEnoughQuantity(quantity)) {
            throw new IllegalArgumentException("상품 수량이 부족합니다.");
        }
    }

    // total price 계산
    private void calculateTotalPrice() {
        this.totalPrice = items.stream().map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 주문상품 조회
    private Optional<OrderItem> findOrderItem(Product product) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();
    }

    // OrderItems 원본 변경방지 - getter 재정의
    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
}