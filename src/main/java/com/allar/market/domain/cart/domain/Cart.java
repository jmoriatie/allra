package com.allar.market.domain.cart.domain;

import com.allar.market.domain.common.BaseEntity;
import com.allar.market.domain.customer.domain.Customer;
import com.allar.market.domain.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Builder
    public Cart(Customer customer){
        this.customer = customer;
    }

    /**
     * 장바구니 담기
     * @param product
     * @param quantity
     */
    public void addItem(Product product, int quantity){
        validateProductQuantity(product, quantity); //수량부족 확인
        Optional<CartItem> foundItem = findCartItem(product.getId());
        if(foundItem.isPresent()){ // 있는 상품일 경우 quantity 더해주기
            foundItem.get().addQuantity(quantity); //
        }else{ // new CartItem
            this.items.add(new CartItem(quantity, product));
        }
    }

    /**
     * 장바구니 삭제
     * @param productId
     */
    public void removeItem(Long productId){
        boolean result = items.removeIf(item -> item.getProduct().getId().equals(productId));
        if(!result){
            throw new IllegalArgumentException("카트에 해당 상품이 없습니다.");
        }
    }

    public CartItem findItem(Long productId){
        return findCartItem(productId)
                .orElseThrow(() -> new IllegalArgumentException("카트에 해당 상품이 없습니다."));
    }

    private Optional<CartItem> findCartItem(Long productId) {
        return this.items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    /**
     * 카트 아이템 수량 변경
     * @param productId
     * @param quantity
     */
    public void updateCartItemQuantity(Long productId, int quantity){
        CartItem foundItem = findCartItem(productId)
                .orElseThrow(() -> new IllegalArgumentException("카트에 해당 상품이 없습니다."));
        validateProductQuantity(foundItem.getProduct(), quantity);
        foundItem.updateQuantity(quantity);
    }

    /**
     * Cart 전체 금액 계산
     * @return CartItems 전체 금액
     */
    public BigDecimal getTotalPrice(){
        return this.items.stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateProductQuantity(Product product, int quantity) {
        if(!product.hasEnoughQuantity(quantity)) {
            throw new IllegalArgumentException("상품 수량이 부족합니다.");
        }
    }

    // CartItems 원본 변경방지 - getter 재정의
    public List<CartItem> getItems() {
        return List.copyOf(this.items);
    }
}
