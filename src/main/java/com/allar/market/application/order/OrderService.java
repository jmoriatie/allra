package com.allar.market.application.order;

import com.allar.market.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // TODO Order 생성 -> 주문하기 창에서 요청하는 기능들
    // TODO cart에 있을 경우 해당 Product, CartItem 세팅, cart에 없을 경우 지나가기
    // TODO 필수! 주문 - 장바구니 합산결제 - 장바구니 로직 필요
    // TODO 필수! 결제완료? 장바구니 관련 로직 - 장바구니 어떻게?
}
