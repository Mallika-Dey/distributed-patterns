package com.example.workflow.delegate;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("prepareOrderDelegate")
public class PrepareOrderDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        Long orderId = ((Number) execution.getVariable("orderId")).longValue();

        Double amount = ((Number) execution.getVariable("amount")).doubleValue();

        String couponCode = (String) execution.getVariable("couponCode");

        log.info("Preparing Order");

        log.info("Order ID : {}", orderId);
        log.info("Amount : {}", amount);
        log.info("Coupon : {}", couponCode);

    }
}