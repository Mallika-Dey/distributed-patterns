package com.example.workflow;

import com.example.workflow.dto.StartWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkflowService {
    private final RuntimeService runtimeService;

    public void start(StartWorkflowRequest request) {

        Map<String, Object> variables = new HashMap<>();

        variables.put("orderId", request.getOrderId());
        variables.put("amount", request.getAmount());
        variables.put("couponCode", request.getCouponCode());

        runtimeService.startProcessInstanceByKey(
                "order",
                variables
        );
    }
}
