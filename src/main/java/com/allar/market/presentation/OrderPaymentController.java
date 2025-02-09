package com.allar.market.presentation;

import com.allar.market.application.orderpayment.OrderPaymentService;
import com.allar.market.application.orderpayment.dto.OrderPaymentRequest;
import com.allar.market.application.orderpayment.dto.OrderPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/payment")
@RequiredArgsConstructor
public class OrderPaymentController {

    private final OrderPaymentService orderPaymentService;

    @PostMapping
    public ResponseEntity<OrderPaymentResponse> processOrderAndPayment(OrderPaymentRequest request){
        OrderPaymentResponse result = orderPaymentService.processOrderAndPayment(request);
        return ResponseEntity.ok(result);
    }
}
