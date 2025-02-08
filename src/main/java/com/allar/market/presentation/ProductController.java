package com.allar.market.presentation;

import com.allar.market.application.product.ProductService;
import com.allar.market.application.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private ProductService productService;

    /**
     * 구매 가능 상품 목록 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findPossiblePurchaseProducts(){
        List<ProductResponse> possiblePurchaseProducts = productService.findPossiblePurchaseProducts();
        return ResponseEntity.ok(possiblePurchaseProducts);
    }
}
