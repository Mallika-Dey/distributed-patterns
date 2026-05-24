package com.example.order.config;

import com.example.order.entity.Order;
import com.example.order.entity.OrderStatus;
import com.example.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "inventory-service.public.outbox_events",
            groupId = "order-group"
    )
    @Transactional
    public void consume(String message) throws Exception {

        JsonNode json =
                objectMapper.readTree(message);
        JsonNode payload = json.get("payload");
        JsonNode after = payload.get("after");

        if (after == null || after.isNull()) {
            return;
        }

        String eventType =
                after.get("event_type").asText();

        JsonNode payloadNode =
                objectMapper.readTree(
                        after.get("payload").asText()
                );

        Long orderId =
                payloadNode.get("orderId").asLong();

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow();

        if (eventType.equals("StockReserved")) {

            order.setStatus(OrderStatus.CONFIRMED);

        } else {

            order.setStatus(OrderStatus.CANCELLED);
        }
    }
}
