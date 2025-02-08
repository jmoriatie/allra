package com.allar.market.presentation;

import com.allar.market.application.cart.CartService;
import com.allar.market.application.cart.dto.CartItemRequest;
import com.allar.market.application.cart.dto.CartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> findCart(@PathVariable Long cartId){
        CartResponse cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Void> addCartItem(
            @PathVariable Long cartId,
            @RequestBody @Valid CartItemRequest request){
        cartService.addCartItem(cartId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long cartId,
            @RequestBody @Valid CartItemRequest request
    ){
        cartService.removeCartItem(cartId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{cartId}/items")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long cartId,
            @RequestBody @Valid CartItemRequest request
    ){
        cartService.updateCartItemQuantity(cartId, request);
        return ResponseEntity.ok().build();
    }
}
