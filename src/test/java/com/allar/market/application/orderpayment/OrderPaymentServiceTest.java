package com.allar.market.application.orderpayment;

import com.allar.market.application.order.OrderService;
import com.allar.market.domain.cart.domain.Cart;
import com.allar.market.domain.cart.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderPaymentServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartRepository cartRepository;

    @Test
    @DisplayName("결제 프로세스 테스트")
    void processOrderAndPayment() {
        // given
        // when
        // then
    }


    @Test
    @DisplayName("Reflection 사용한 Entity ID 값 주입 테스트 - Mock Test시 활용")
    void injectionIdTest() throws NoSuchFieldException, IllegalAccessException {
        Cart savedEntity = Cart.builder().build();
        Field idField = savedEntity.getClass().getDeclaredField("id");

        idField.setAccessible(true);
        idField.set(savedEntity, 1L);

        assertEquals(1L, savedEntity.getId());
    }
}