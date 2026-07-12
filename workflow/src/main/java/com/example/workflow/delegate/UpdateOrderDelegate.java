package com.example.workflow.delegate;

import com.example.workflow.client.OrderClient;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("updateOrderDelegate")
@RequiredArgsConstructor
public class UpdateOrderDelegate implements JavaDelegate {
    private final OrderClient orderClient;

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId =
                ((Number) execution.getVariable("orderId")).longValue();

        orderClient.complete(orderId);

    }

}
