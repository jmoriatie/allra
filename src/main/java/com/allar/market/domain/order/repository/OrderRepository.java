package com.allar.market.domain.order.repository;

import com.allar.market.domain.order.domain.Order;
import com.allar.market.domain.order.domain.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndOrderStateNot(Long id, OrderState orderState);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.customer.id = :customerId")
    List<Order> findOrderWithCustomerByCustomerId(@Param("customerId") Long customerId);
}
