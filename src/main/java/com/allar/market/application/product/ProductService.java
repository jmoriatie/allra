package com.allar.market.application.product;

import com.allar.market.application.product.dto.ProductResponse;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> findPossiblePurchaseProducts(){
        List<Product> possiblePurchaseProducts = productRepository.findPossiblePurchaseProducts();
        return possiblePurchaseProducts.stream().map(ProductResponse::from).toList();
    }
}
