package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.entity.Order;
import com.example.order.entity.OrderStatus;
import com.example.order.entity.OutboxEvent;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.OutboxRepository;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void createOrder(CreateOrderRequest request) throws Exception {

        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        Map<String, Object> event = Map.of(
                "orderId", order.getId(),
                "productId", order.getProductId(),
                "quantity", order.getQuantity()
        );

        OutboxEvent outbox = OutboxEvent.builder()
                .aggregateType("Order")
                .aggregateId(order.getId().toString())
                .eventType("OrderCreated")
                .payload(objectMapper.writeValueAsString(event))
                .createdAt(LocalDateTime.now())
                .build();

        outboxRepository.save(outbox);
    }
}
