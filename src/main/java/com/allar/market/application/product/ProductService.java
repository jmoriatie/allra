package com.allar.market.application.product;

import com.allar.market.application.product.dto.ProductResponse;
import com.allar.market.domain.order.domain.OrderItem;
import com.allar.market.domain.product.domain.Product;
import com.allar.market.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 구매가능 상품 조회
     *
     * @return 수량 > 0개 상품리스트
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> findPossiblePurchaseProducts() {
        List<Product> possiblePurchaseProducts = productRepository.findPossiblePurchaseProducts();
        return possiblePurchaseProducts.stream().map(ProductResponse::from).toList();
    }

    /**
     * 주문완료 상품 수량 감소
     * @param orderItems
     */
    public void decreaseOrderedQuantity(List<OrderItem> orderItems) {
        Set<Long> productIds = orderItems.stream().map(item ->
                        item.getProduct().getId())
                .collect(Collectors.toSet());
        List<Product> products = productRepository.findAllById(productIds);

        for(Product product : products){
            for(OrderItem orderItem: orderItems){
                if(orderItem.getProduct().getId().equals(product.getId())){
                    product.decreaseQuantity(orderItem.getQuantity());
                }
            }
        }
    }
}
