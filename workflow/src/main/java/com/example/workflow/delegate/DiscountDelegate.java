package com.example.workflow.delegate;

import com.example.workflow.client.DiscountClient;
import com.example.workflow.client.PaymentClient;
import com.example.workflow.dto.DiscountRequest;
import com.example.workflow.dto.DiscountResponse;
import com.example.workflow.dto.PaymentRequest;
import com.example.workflow.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("discountDelegate")
@RequiredArgsConstructor
@Slf4j
public class DiscountDelegate implements JavaDelegate {

    private final DiscountClient discountClient;

    @Override
    public void execute(DelegateExecution execution) {

        DiscountRequest request = DiscountRequest.builder()
                .orderId(
                        ((Number) execution.getVariable("orderId")).longValue())
                .amount(
                        ((Number) execution.getVariable("amount")).doubleValue())
                .couponCode(
                        (String) execution.getVariable("couponCode"))
                .build();

        DiscountResponse response = discountClient.apply(request);

        execution.setVariable("discount", response.getDiscount());
        execution.setVariable("finalAmount", response.getFinalAmount());

        log.info("Discount Service Returned {}", response);

    }

}