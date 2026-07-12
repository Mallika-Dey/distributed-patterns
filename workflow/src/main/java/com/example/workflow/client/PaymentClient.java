package com.example.workflow.client;

import com.example.workflow.dto.DiscountRequest;
import com.example.workflow.dto.DiscountResponse;
import com.example.workflow.dto.PaymentRequest;
import com.example.workflow.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final RestTemplate restTemplate;

    public PaymentResponse apply(PaymentRequest request) {
        return restTemplate.postForObject(
                "http://localhost:8082/payment",
                request,
                PaymentResponse.class
        );
    }
}
