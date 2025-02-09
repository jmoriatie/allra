package com.allar.market.application.orderpayment;

import com.allar.market.application.cart.CartService;
import com.allar.market.application.order.OrderService;
import com.allar.market.application.orderpayment.dto.OrderPaymentRequest;
import com.allar.market.application.orderpayment.dto.OrderPaymentResponse;
import com.allar.market.application.payment.PaymentService;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.payment.domain.Payment;
import com.allar.market.domain.payment.domain.PaymentMethod;
import com.allar.market.global.exception.exceptions.PaymentProcessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderPaymentService {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final CartService cartService;

    public OrderPaymentResponse processOrderAndPayment(OrderPaymentRequest request){
        try{
            // 결제 진행
            PaymentMethod paymentMethod = PaymentMethod.valueOf(request.paymentMethod().toUpperCase());
            Payment payment = paymentService.processPayment(request.orderId(), paymentMethod);

            // 주문아이템 수량 지우기
            Order order = payment.getOrder();
            orderService.completePayment(order.getId());

            // 카트아이템 수량 지우기
            Cart cart = order.getCustomer().getCart();
            cartService.removeOrderedCartItem(cart.getId(), order.getItems());

            return OrderPaymentResponse.builder()
                    .status(payment.getState().name())
                    .transactionId(payment.getTransactionId())
                    .message("SUCCESS")
                    .build();
        }catch (Exception e){
            throw new PaymentProcessException("결제 중 오류가 발생했습니다.");
        }
    }
}
