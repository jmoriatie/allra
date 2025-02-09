package com.allar.market.domain.payment.domain;

import com.allar.market.domain.common.BaseEntity;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.global.exception.exceptions.PaymentClosedException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(value = EnumType.STRING)
    private PaymentState state;
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod method;
    private BigDecimal price;

    @Builder
    public Payment(PaymentMethod method, BigDecimal price, Order order) {
        this.price = price;
        this.method = method;
        this.order = order;
        this.state = PaymentState.WAITING;
    }

    public void complete(){
        isClosedPayment();
        this.state = PaymentState.COMPLETED;
    }

    public void fail(){
        isClosedPayment();
        this.state = PaymentState.FAILED;
    }

    private void isClosedPayment() {
        if(this.state.isClosed()){
            throw new PaymentClosedException("이미 종료된 결제건 입니다.");
        }
    }
}
