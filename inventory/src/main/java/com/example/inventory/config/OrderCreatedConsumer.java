package com.example.inventory.config;

import com.example.inventory.entity.Inventory;
import com.example.inventory.entity.OutboxEvent;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {
    private final InventoryRepository inventoryRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order-service.public.outbox_events",
            groupId = "inventory-group"
    )
    @Transactional
    public void consume(String message) throws Exception {
        log.info("Inventory consuming message");
        JsonNode json =
                objectMapper.readTree(message);
        JsonNode payload = json.get("payload");
        JsonNode after = payload.get("after");

        if (after == null || after.isNull()) {
            return;
        }

        JsonNode payloadNode =
                objectMapper.readTree(
                        after.get("payload").asText()
                );

        Long productId =
                payloadNode.get("productId").asLong();

        Integer quantity =
                payloadNode.get("quantity").asInt();

        Long orderId =
                payloadNode.get("orderId").asLong();

        Inventory inventory =
                inventoryRepository
                        .findById(productId)
                        .orElse(null);

        String eventType;

        if (!ObjectUtils.isEmpty(inventory) && inventory.getAvailableQuantity() >= quantity) {

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity()
                            - quantity
            );

            inventoryRepository.save(inventory);

            eventType = "StockReserved";

        } else {

            eventType = "StockFailed";
        }

        Map<String, Object> event = Map.of(
                "orderId", orderId
        );

        OutboxEvent outbox = OutboxEvent.builder()
                .aggregateType("Inventory")
                .aggregateId(orderId.toString())
                .eventType(eventType)
                .payload(
                        objectMapper.writeValueAsString(event)
                )
                .createdAt(LocalDateTime.now())
                .build();

        outboxRepository.save(outbox);
    }
}
