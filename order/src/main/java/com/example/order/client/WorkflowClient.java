package com.example.order.client;

import com.example.order.dto.StartWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WorkflowClient {

    private final RestTemplate restTemplate;

    public void start(StartWorkflowRequest request) {

        restTemplate.postForEntity(
                "http://localhost:8080/workflow/start",
                request,
                String.class
        );

    }

}