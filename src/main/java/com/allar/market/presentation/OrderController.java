package com.allar.market.presentation;

import com.allar.market.application.order.OrderService;
import com.allar.market.application.order.dto.CartOrderRequest;
import com.allar.market.application.order.dto.OrderRequest;
import com.allar.market.application.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderWithoutComplete(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderWithoutComplete(orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> createOrderFromCart(@Valid @RequestBody CartOrderRequest cartOrderRequest) {
        OrderResponse order = orderService.createOrderFromCart(cartOrderRequest);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);
        return ResponseEntity.ok().build();
    }
}
