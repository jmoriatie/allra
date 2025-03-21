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


// TODO 해당 서비스 -> domain service 로 이동 // application layer -> domain layer (domain service)
//  ㄴ domain 에는 해당 서비스 받는 도메인로직 만들기
//  ㄴ 이후 PaymentService layer(or 특정 서비스)에서 해당 서비스 domain 에 DI 해주기
//  ㄴ 이유는? 여러가지 도메인이 얽혀서 한가지 도메인에 책임을 주기 어려움 + 해당 부분 도메인 로직이기 때문(애그리거트 값 변경 및 계산로직 있음)
//  ㄴ 통합테스트 작성
//  ㄴ README 수정 + 요구사항도 같이 추가
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
