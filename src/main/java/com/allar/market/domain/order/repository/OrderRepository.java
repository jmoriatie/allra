package com.allar.market.domain.order.repository;

import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.domain.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndOrderStateNot(Long id, OrderState orderState);
}
