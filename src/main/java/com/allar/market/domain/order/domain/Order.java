package com.allar.market.domain.order.domain;

import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.common.BaseEntity;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.payment.domain.Payment;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.global.exception.exceptions.OrderCancelException;
import com.allar.market.global.exception.exceptions.ProductQuantityNotEnoughException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;
    private BigDecimal totalPrice;

    @Setter
    private String receiver;

    @Builder
    public Order(Customer customer) {
        setCustomer(customer);
        this.totalPrice = BigDecimal.ZERO;
        this.orderState = OrderState.WAITING;
    }

    private void setCustomer(Customer customer) {
        if (customer != null) {
            this.customer = customer;
            customer.addOrder(this);
        }
    }

    /**
     * 장바구니(Cart)에서 수량 가져오는 로직 추가
     *
     * @param cart
     */
    public void addFromCart(Cart cart) {
        // cart totalPrice -> order totalPrice
        this.totalPrice = cart.getTotalPrice();
        // cartItems -> orderItems
        cart.getItems().forEach(cartItem -> {
            addOrderItemsWithSetOrder(OrderItem.addFromCartItem(cartItem));
        });
    }

    // Order-OrderItem 양방향 세팅
    private void addOrderItemsWithSetOrder(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 상품 주문
     *
     * @param product
     * @param quantity
     */
    public void addItem(Product product, int quantity) {
        validateProductQuantity(product, quantity); // 재고확인
        addOrderItemsWithSetOrder(new OrderItem(product, quantity));
        calculateTotalPrice();
    }

    /**
     * 주문 상태 업데이트
     *
     * @param orderState
     */
    public void updateOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    /**
     * 주문완료
     */
    public void completePayment() {
        updateOrderState(OrderState.PAID);
    }

    /**
     * 주문취소
     */
    public void cancel() {
        isCancellable();
        updateOrderState(OrderState.CANCELLED);
    }

    // 주문 취소 가능여부 조회
    private void isCancellable() {
        if (!this.orderState.isCancellable()) {
            throw new OrderCancelException("주문을 취소할 수 없습니다.");
        }
    }

    // 상품 재고 조회
    private void validateProductQuantity(Product product, int quantity) {
        if (!product.hasEnoughQuantity(quantity)) {
            throw new ProductQuantityNotEnoughException("상품 수량이 부족합니다.");
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