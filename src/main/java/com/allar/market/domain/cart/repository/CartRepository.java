package com.allar.market.domain.cart.repository;

import com.allar.market.domain.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
