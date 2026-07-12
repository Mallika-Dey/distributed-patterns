package com.example.workflow.delegate;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("paymentDelegate")
public class PaymentDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        Double amount;

        Object value = execution.getVariable("finalAmount");

        if (value == null) {
            amount = ((Number) execution.getVariable("amount")).doubleValue();
        } else {
            amount = ((Number) value).doubleValue();
        }

        log.info("Processing payment");
        log.info("Amount : {}", amount);

        execution.setVariable("paymentStatus", "SUCCESS");

    }

}