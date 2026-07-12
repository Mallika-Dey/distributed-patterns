package com.example.order;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.entity.Order;
import com.example.order.entity.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPersistenceService {
    private final OrderRepository repository;

    @Transactional
    public Order save(CreateOrderRequest request) {
        Order order = Order.builder()
                .amount(request.getAmount())
                .couponCode(request.getCouponCode())
                .status(OrderStatus.PROCESSING)
                .build();

        return repository.save(order);
    }
}
