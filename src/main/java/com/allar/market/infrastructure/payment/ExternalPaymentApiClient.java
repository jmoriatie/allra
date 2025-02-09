package com.allar.market.infrastructure.payment;

import com.allar.market.infrastructure.payment.dto.PaymentApiRequest;
import com.allar.market.infrastructure.payment.dto.PaymentApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExternalPaymentApiClient {

    private final WebClient webClient;

    @Value("${external.payment.api.url}")
    private String apiUrl;

    /**
     * 외부 결제요청 API
     * @param request
     * @return PaymentApiResponse
     */
    public PaymentApiResponse processPayment(PaymentApiRequest request){
        return webClient.post()
                .uri(apiUrl+"/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PaymentApiRequest.class)
                .retrieve()
                .bodyToMono(PaymentApiResponse.class)
                .block();
    }
}
