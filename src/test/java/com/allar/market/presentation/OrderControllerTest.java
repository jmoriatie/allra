package com.allar.market.presentation;

import com.allar.market.application.order.OrderService;
import com.allar.market.application.order.dto.CartOrderRequest;
import com.allar.market.application.order.dto.OrderItemResponse;
import com.allar.market.application.order.dto.OrderResponse;
import com.allar.market.global.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createOrder_returnSuccess() throws Exception {
        // given
        CartOrderRequest request = new CartOrderRequest(1L);

        // OrderResponse 객체 생성
        List<OrderItemResponse> orderItems = List.of(
                OrderItemResponse.builder()
                        .productId(101L)
                        .productName("상품 1")
                        .price(new BigDecimal("10000"))
                        .quantity(2)
                        .totalPrice(new BigDecimal("20000"))
                        .build(),
                OrderItemResponse.builder()
                        .productId(102L)
                        .productName("상품 2")
                        .price(new BigDecimal("5000"))
                        .quantity(1)
                        .totalPrice(new BigDecimal("5000"))
                        .build()
        );

        OrderResponse expectedResponse = OrderResponse.builder()
                .id(1L)
                .orderItems(orderItems)
                .totalPrice(new BigDecimal("25000"))
                .build();

        // 서비스 계층의 CompletableFuture 반환값 mocking
        when(orderService.createOrderFromCart(any(CartOrderRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(expectedResponse));


        MvcResult mvcResult = mockMvc.perform(post("/api/order/from-cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        // When & Then
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderItems", hasSize(2)))
                .andExpect(jsonPath("$.orderItems[0].productId").value(101))
                .andExpect(jsonPath("$.orderItems[0].productName").value("상품 1"))
                .andExpect(jsonPath("$.orderItems[0].price").value(10000))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(2))
                .andExpect(jsonPath("$.orderItems[0].totalPrice").value(20000))
                .andExpect(jsonPath("$.orderItems[1].productId").value(102))
                .andExpect(jsonPath("$.totalPrice").value(25000))
                .andDo(print());

        verify(orderService).createOrderFromCart(any(CartOrderRequest.class));
    }
}