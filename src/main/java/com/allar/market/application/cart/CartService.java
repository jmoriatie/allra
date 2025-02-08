package com.allar.market.application.cart;

import com.allar.market.application.cart.dto.CartItemRequest;
import com.allar.market.application.cart.dto.CartResponse;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.repository.CartRepository;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CartResponse findCart(Long cartId){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("없는 장바구니 ID 입니다."));

        return CartResponse.from(cart);
    }
    public void addCartItem(Long cartId, CartItemRequest request){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("없는 장바구니 ID 입니다."));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("없는 상품 입니다."));
        cart.addItem(product, request.quantity());
    }

    public void removeCartItem(Long cartId, CartItemRequest request){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("없는 장바구니 ID 입니다."));
        cart.removeItem(request.productId());
    }

    public void updateCartItemQuantity(Long cartId, CartItemRequest request){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("없는 장바구니 ID 입니다."));
        cart.updateCartItemQuantity(request.productId(), request.quantity());
    }
}
