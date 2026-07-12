package com.example.workflow.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OrderClient {

    private final RestTemplate restTemplate;

    public void complete(Long orderId) {

        restTemplate.put(
                "http://localhost:8083/orders/" + orderId + "/complete",
                null
        );

    }

}
