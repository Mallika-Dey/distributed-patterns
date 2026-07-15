package com.example.order;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.entity.Order;
import com.example.order.entity.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository repository;
    private final RuntimeService runtimeService;

    public Order create(CreateOrderRequest request) {

        Order order = repository.save(
                Order.builder()
                        .amount(request.getAmount())
                        .couponCode(request.getCouponCode())
                        .status(OrderStatus.CREATED)
                        .build()
        );

        Map<String, Object> variables = new HashMap<>();

        variables.put("orderId", order.getId());
        variables.put("amount", order.getAmount());
        variables.put("couponCode", order.getCouponCode());

        variables.put(
                "couponPresent",
                order.getCouponCode() != null &&
                        !order.getCouponCode().isBlank()
        );

        runtimeService.startProcessInstanceByKey(
                "orderProcessAsync",
                variables
        );

        return order;
    }
}