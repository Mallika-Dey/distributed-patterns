package com.example.workflow.delegate;

import com.example.workflow.client.PaymentClient;
import com.example.workflow.dto.PaymentRequest;
import com.example.workflow.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("paymentDelegate")
@RequiredArgsConstructor
public class PaymentDelegate implements JavaDelegate {
    private final PaymentClient paymentClient;

    @Override
    public void execute(DelegateExecution execution) {

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(
                        ((Number) execution.getVariable("orderId")).longValue())
                .amount(
                        ((Number) execution.getVariable("amount")).doubleValue())
                .build();

        PaymentResponse paymentResponse = paymentClient.apply(paymentRequest);

        log.info("Payment Service Returned {}", paymentResponse);

        execution.setVariable("paymentStatus", "SUCCESS");

    }

}