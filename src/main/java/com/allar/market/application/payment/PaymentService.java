package com.allar.market.application.payment;

import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.repository.OrderRepository;
import com.allar.market.domain.payment.domain.Payment;
import com.allar.market.domain.payment.domain.PaymentMethod;
import com.allar.market.domain.payment.repository.PaymentRepository;
import com.allar.market.global.exception.exceptions.PaymentProcessException;
import com.allar.market.infrastructure.payment.ExternalPaymentApiClient;
import com.allar.market.infrastructure.payment.dto.PaymentApiRequest;
import com.allar.market.infrastructure.payment.dto.PaymentApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ExternalPaymentApiClient paymentApiClient; // TODO interface화 하고, 구현체 Infrastructure로 이동

    // TODO lock
    /**
     * 결제 프로세스
     * @param orderId
     * @return
     */
    public Payment processPayment(Long orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("없는 주문 ID 입니다."));

        Payment payment = createNewPayment(method, order);

        try {
            PaymentApiResponse result = paymentApiClient.processPayment(PaymentApiRequest.from(payment));

            if(result.isSuccess()){
                payment.complete(result.transactionId());
            } else{
                payment.fail();
                throw new PaymentProcessException("결제 중 오류 발생");
            }
            return paymentRepository.save(payment);
        }catch (Exception e){
            payment.fail();
            throw new PaymentProcessException("결제 중 오류 발생");
        }
    }

    private Payment createNewPayment(PaymentMethod method, Order order) {
        return Payment.builder()
                .order(order)
                .price(order.getTotalPrice())
                .method(method)
                .build();
    }
}
