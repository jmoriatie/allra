package com.allar.market.domain.order.repository;

import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.domain.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndOrderStateNot(Long id, OrderState orderState);
}
