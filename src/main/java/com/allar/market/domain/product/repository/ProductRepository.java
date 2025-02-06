package com.allar.market.domain.product.repository;

import com.allar.market.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.quantity > 0")
    List<Product> findPossiblePurchaseProducts();
}
