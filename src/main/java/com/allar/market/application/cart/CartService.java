package com.allar.market.application.cart;

import com.allar.market.application.cart.dto.CartItemRequest;
import com.allar.market.application.cart.dto.CartResponse;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.repository.CartRepository;
import com.allar.market.domain.order.domain.OrderItem;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public CartResponse getCart(Long cartId){
        Cart cart = findCart(cartId);
        return CartResponse.from(cart);
    }

    public void addCartItem(Long cartId, CartItemRequest request){
        Cart cart = findCart(cartId);
        Product product = findProduct(request);
        cart.addItem(product, request.quantity());
    }

    public void removeCartItem(Long cartId, CartItemRequest request){
        Cart cart = findCart(cartId);
        cart.removeItem(request.productId());
    }

    public void updateCartItemQuantity(Long cartId, CartItemRequest request){
        Cart cart = findCart(cartId);
        cart.updateCartItemQuantity(request.productId(), request.quantity());
    }

    public void removeOrderedCartItem(Long cartId, List<OrderItem> orderItems){
        Cart cart = findCart(cartId);
        cart.removeOrderedCartItem(orderItems);
    }

    private Cart findCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("없는 장바구니 ID 입니다."));
    }

    private Product findProduct(CartItemRequest request) {
        return productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("없는 상품 입니다."));
    }
}
