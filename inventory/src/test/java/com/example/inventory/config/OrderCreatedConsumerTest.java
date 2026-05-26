package com.example.inventory.config;

import com.example.inventory.entity.Inventory;
import com.example.inventory.entity.OutboxEvent;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderCreatedConsumerTest {

    private final InventoryRepository inventoryRepository =
            mock(InventoryRepository.class);
    private final OutboxRepository outboxRepository =
            mock(OutboxRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final OrderCreatedConsumer consumer =
            new OrderCreatedConsumer(
                    inventoryRepository,
                    outboxRepository,
                    objectMapper
            );

    @Test
    void ignoresNonOrderCreatedEvents() throws Exception {
        consumer.consume(debeziumMessage("OrderCancelled", 1L, 101L, 2));

        verify(inventoryRepository, never()).findById(any());
        verify(outboxRepository, never())
                .existsByAggregateTypeAndAggregateId(any(), any());
        verify(outboxRepository, never()).save(any());
    }

    @Test
    void skipsDuplicateOrdersThatAlreadyPublishedInventoryEvent() throws Exception {
        when(outboxRepository.existsByAggregateTypeAndAggregateId(
                "Inventory",
                "101"
        )).thenReturn(true);

        consumer.consume(debeziumMessage("OrderCreated", 1L, 101L, 2));

        verify(inventoryRepository, never()).findById(any());
        verify(outboxRepository, never()).save(any());
    }

    @Test
    void reservesStockAndPublishesOutboxEventForFirstOrderCreated() throws Exception {
        Inventory inventory = new Inventory(1L, 10);

        when(outboxRepository.existsByAggregateTypeAndAggregateId(
                "Inventory",
                "101"
        )).thenReturn(false);
        when(inventoryRepository.findById(1L))
                .thenReturn(Optional.of(inventory));

        consumer.consume(debeziumMessage("OrderCreated", 1L, 101L, 3));

        assertEquals(7, inventory.getAvailableQuantity());
        verify(inventoryRepository).save(inventory);
        verify(outboxRepository).save(any(OutboxEvent.class));
    }

    @Test
    void publishesStockFailedWhenInventoryMissing() throws Exception {
        when(outboxRepository.existsByAggregateTypeAndAggregateId(
                "Inventory",
                "101"
        )).thenReturn(false);
        when(inventoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        consumer.consume(debeziumMessage("OrderCreated", 1L, 101L, 3));

        verify(inventoryRepository, never()).save(any());
        verify(outboxRepository).save(any(OutboxEvent.class));
    }

    private String debeziumMessage(
            String eventType,
            Long productId,
            Long orderId,
            Integer quantity
    ) throws Exception {
        String outboxPayload = objectMapper.writeValueAsString(
                java.util.Map.of(
                        "productId", productId,
                        "orderId", orderId,
                        "quantity", quantity
                )
        );

        String message = objectMapper.writeValueAsString(
                java.util.Map.of(
                        "payload",
                        java.util.Map.of(
                                "after",
                                java.util.Map.of(
                                        "event_type", eventType,
                                        "payload", outboxPayload
                                )
                        )
                )
        );

        assertTrue(message.contains(eventType));
        return message;
    }
}
