package com.example.workflow.client;

import com.example.workflow.dto.DiscountRequest;
import com.example.workflow.dto.DiscountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DiscountClient {

    private final RestTemplate restTemplate;

    public DiscountResponse apply(DiscountRequest request) {

        return restTemplate.postForObject(
                "http://localhost:8081/discount/apply",
                request,
                DiscountResponse.class
        );
    }
}
