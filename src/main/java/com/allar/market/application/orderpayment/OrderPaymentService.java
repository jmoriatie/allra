package com.allar.market.application.orderpayment;

import com.allar.market.application.cart.CartService;
import com.allar.market.application.order.OrderService;
import com.allar.market.application.orderpayment.dto.OrderPaymentRequest;
import com.allar.market.application.orderpayment.dto.OrderPaymentResponse;
import com.allar.market.application.payment.PaymentService;
import com.allar.market.application.product.ProductService;
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
    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;

    public OrderPaymentResponse processOrderAndPayment(OrderPaymentRequest request){
        try{
            Payment payment = paymentService.processPayment(request.orderId(), getPaymentMethod(request));

            if(payment.isComplete()){ // 결제 성공시
                orderPaymentSuccessProcess(payment);
            }

            return OrderPaymentResponse.builder()
                    .status(payment.getState().name())
                    .transactionId(payment.getTransactionId())
                    .message(payment.isComplete()? "SUCCESS" : "FAILED")
                    .build();
        }catch (Exception e){
            throw new PaymentProcessException("결제 중 오류가 발생했습니다.");
        }
    }

    private PaymentMethod getPaymentMethod(OrderPaymentRequest request) {
        return PaymentMethod.valueOf(request.paymentMethod().toUpperCase());
    }

    private void orderPaymentSuccessProcess(Payment payment) {
        // 주문아이템 수량 지우기
        Order order = payment.getOrder();
        orderService.completePayment(order.getId());

        // 주문아이템 기준 - 상품 수량 지우기
        productService.decreaseOrderedQuantity(order.getItems());

        // 주문아이템 기준 - 카트아이템 수량 지우기
        Cart cart = order.getCustomer().getCart();
        cartService.removeOrderedCartItem(cart.getId(), order.getItems());
    }
}
