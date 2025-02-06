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

    /**
     * 구매가능 상품 조회
     * @return 수량 > 0개 상품리스트
     */
    public List<ProductResponse> findPossiblePurchaseProducts(){
        List<Product> possiblePurchaseProducts = productRepository.findPossiblePurchaseProducts();
        return possiblePurchaseProducts.stream().map(ProductResponse::from).toList();
    }
}
