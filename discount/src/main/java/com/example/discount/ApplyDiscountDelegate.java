package com.example.discount;

import com.example.discount.dto.ApplyDiscountCommand;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("applyDiscountDelegate")
@RequiredArgsConstructor
public class ApplyDiscountDelegate implements JavaDelegate {

    private final DiscountService discountService;

    @Override
    public void execute(DelegateExecution execution) {

        Long orderId = getRequiredVariable(execution, "orderId", Long.class);
        Double amount = getRequiredVariable(execution, "amount", Double.class);
        String couponCode = execution.getVariable("couponCode", String.class);

        discountService.apply(
                ApplyDiscountCommand.builder()
                        .orderId(orderId)
                        .amount(amount)
                        .couponCode(couponCode)
                        .build()
        );
    }

    private <T> T getRequiredVariable(DelegateExecution execution,
                                      String variableName,
                                      Class<T> type) {

        T value = execution.getVariable(variableName, type);

        if (value == null) {
            throw new IllegalArgumentException(
                    "Missing required process variable: " + variableName
            );
        }

        return value;
    }
}