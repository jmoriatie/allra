package com.allar.market.domain.common.cache;

import com.allar.market.application.order.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

//TODO 캐시 redis 로 변경해보기
@Component
public class OrderResultCache {
    private final Map<Long, CompletableFuture<OrderResponse>> cache = new ConcurrentHashMap<>();

    public void put(Long cartId, CompletableFuture<OrderResponse> future){
        cache.put(cartId, future);
    }

    public CompletableFuture<OrderResponse> get(Long cartId){
        return cache.get(cartId);
    }

    public void remove(Long cartId){
        cache.remove(cartId);
    }
}
